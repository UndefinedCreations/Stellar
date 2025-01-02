package com.undefined.stellar.v1_16_1

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.help.CustomCommandHelpTopic
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.server.v1_16_R1.CommandListenerWrapper
import net.minecraft.server.v1_16_R1.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

@Suppress("DEPRECATION")
object BrigadierCommandHelper {

    val COMMAND_SOURCE: CommandListenerWrapper by lazy {
        MinecraftServer.getServer().serverCommandListener
    }
    val dispatcher: CommandDispatcher<CommandListenerWrapper> by lazy {
        MinecraftServer.getServer().functionData.commandDispatcher
    }

    fun register(command: LiteralArgumentBuilder<CommandListenerWrapper>): LiteralCommandNode<CommandListenerWrapper>? =
        dispatcher.register(command)

    fun handleHelpTopic(command: AbstractStellarCommand<*>) {
        Bukkit.getServer().helpMap.addTopic(
            CustomCommandHelpTopic(command.name, command.description, command.helpTopic) {
                val context = MinecraftServer.getServer().serverCommandListener
                val requirements = command.requirements.all { it(this) }
                val permissionRequirements = command.permissionRequirements.all {
                    if (it.permission.isEmpty()) context.hasPermission(it.level)
                    else context.hasPermission(it.level, it.permission)
                }
                requirements.and(permissionRequirements)
            }
        )
    }

    fun handleExecutions(command: AbstractStellarCommand<*>, context: CommandContext<CommandListenerWrapper>) {
        val stellarContext = CommandContextAdapter.getStellarCommandContext(context)

        for (runnable in command.base.runnables) runnable(stellarContext)
        val arguments = getArguments(command.base, context)
        for (argument in arguments) for (runnable in argument.runnables) runnable(stellarContext)
        for (execution in command.executions) execution(stellarContext)
    }

    fun fulfillsRequirements(command: AbstractStellarCommand<*>, source: CommandListenerWrapper): Boolean {
        val fulfillsExecutionRequirements = command.requirements.all { it(source.bukkitSender) }
        val fulfillsPermissionRequirements = command.permissionRequirements.all { source.hasPermission(it.level, it.permission) }
        return fulfillsExecutionRequirements.and(fulfillsPermissionRequirements)
    }

    fun handleFailureMessageAndExecutions(command: AbstractStellarCommand<*>, context: CommandContext<CommandListenerWrapper>) {
        for (execution in command.failureExecutions) execution(CommandContextAdapter.getStellarCommandContext(context))
        for (message in command.failureMessages) context.source.bukkitSender.sendMessage(LegacyComponentSerializer.legacySection().serialize(message))
        for (message in command.globalFailureMessages) context.source.bukkitSender.sendMessage(LegacyComponentSerializer.legacySection().serialize(message))
    }

    fun getArguments(
        baseCommand: AbstractStellarCommand<*>,
        context: CommandContext<CommandListenerWrapper>,
        currentIndex: Int = 1,
        listOfArguments: List<AbstractStellarArgument<*>> = emptyList()
    ): List<AbstractStellarArgument<*>> {
        if (listOfArguments.size == context.nodes.size - 1) return listOfArguments
        for (argument in baseCommand.arguments)
            if (argument.name == context.nodes[currentIndex].node.name)
                return getArguments(argument, context, currentIndex + 1, listOfArguments + argument)
        return emptyList()
    }

}

fun sync(execution: () -> Unit) {
    object : BukkitRunnable() {
        override fun run() = execution()
    }.runTask(CommandRegistrar.plugin)
}