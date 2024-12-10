package com.undefined.stellar.v1_21_3

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer

object BrigadierCommandHelper {

    val COMMAND_SOURCE: CommandSourceStack by lazy {
        MinecraftServer.getServer().createCommandSourceStack()
    }
    val dispatcher by lazy { MinecraftServer.getServer().functions.dispatcher }

    fun register(command: LiteralArgumentBuilder<CommandSourceStack>): LiteralCommandNode<CommandSourceStack>? =
        dispatcher.register(command)

    fun handleExecutions(command: AbstractStellarCommand<*>, context: CommandContext<CommandSourceStack>) {
        val stellarContext = CommandContextAdapter.getStellarCommandContext(context)

        for (runnable in command.base.runnables) runnable(stellarContext)
        val arguments = getArguments(command.base, context)
        for (argument in arguments) for (runnable in argument.runnables) runnable(stellarContext)
        for (execution in command.executions) execution(stellarContext)
    }

    fun fulfillsRequirements(command: AbstractStellarCommand<*>, source: CommandSourceStack): Boolean {
        val fulfillsExecutionRequirements = command.requirements.all { it(source.bukkitSender) }
        val fulfillsPermissionRequirements = command.permissionRequirements.all { source.hasPermission(it.level, it.permission) }
        return fulfillsExecutionRequirements.and(fulfillsPermissionRequirements)
    }

    fun handleFailureMessageAndExecutions(command: AbstractStellarCommand<*>, context: CommandContext<CommandSourceStack>) {
        for (execution in command.failureExecutions) execution(CommandContextAdapter.getStellarCommandContext(context))
        for (message in command.failureMessages) context.source.bukkitSender.sendMessage(LegacyComponentSerializer.legacySection().serialize(message))
        for (message in command.globalFailureMessages) context.source.bukkitSender.sendMessage(LegacyComponentSerializer.legacySection().serialize(message))
    }

    fun getArguments(
        baseCommand: AbstractStellarCommand<*>,
        context: CommandContext<CommandSourceStack>,
        currentIndex: Int = 1,
        listOfSubCommands: List<AbstractStellarArgument<*>> = emptyList()
    ): List<AbstractStellarArgument<*>> {
        if (listOfSubCommands.size == context.nodes.size - 1) return listOfSubCommands
        for (subCommand in baseCommand.arguments) {
            if (subCommand.name == context.nodes[currentIndex].node.name) {
                return getArguments(subCommand, context, currentIndex + 1, listOfSubCommands + subCommand)
            }
        }
        return emptyList()
    }

}