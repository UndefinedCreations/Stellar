package com.undefined.stellar.v1_16_4

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.help.CustomCommandHelpTopic
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import net.minecraft.server.v1_16_R3.MinecraftServer
import org.bukkit.Bukkit
import java.util.concurrent.CompletableFuture

@Suppress("DEPRECATION")
object BrigadierCommandHelper {

    val COMMAND_SOURCE: CommandListenerWrapper by lazy {
        MinecraftServer.getServer().serverCommandListener
    }

    fun handleHelpTopic(command: AbstractStellarCommand<*>) {
        Bukkit.getServer().helpMap.addTopic(
            CustomCommandHelpTopic(command.name, command.description, command.information) {
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

    fun handleSuggestions(
        command: AbstractStellarArgument<*, *>,
        context: CommandContext<CommandListenerWrapper>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val stellarContext = CommandContextAdapter.getStellarCommandContext(context)
        val completions: CompletableFuture<List<com.undefined.stellar.data.suggestion.Suggestion>> = CompletableFuture.supplyAsync {
            val suggestions: MutableList<com.undefined.stellar.data.suggestion.Suggestion> = mutableListOf()
            for (suggestion in command.suggestions) suggestions.addAll(suggestion.get(stellarContext, builder.remaining).get())
            suggestions
        }
        return CommandAdapter.getMojangSuggestions(builder, completions)
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
        listOfArguments: List<AbstractStellarArgument<*, *>> = emptyList()
    ): List<AbstractStellarArgument<*, *>> {
        if (listOfArguments.size == context.nodes.size - 1) return listOfArguments
        for (argument in baseCommand.arguments)
            if (argument.name == context.nodes[currentIndex].node.name)
                return getArguments(argument, context, currentIndex + 1, listOfArguments + argument)
        return emptyList()
    }

}
