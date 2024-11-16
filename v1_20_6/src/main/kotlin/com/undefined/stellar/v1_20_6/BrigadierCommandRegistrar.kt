package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.StellarCommand
import com.undefined.stellar.StellarCommands
import com.undefined.stellar.exception.UnsupportedSubCommandException
import com.undefined.stellar.sub.AbstractStellarSubCommand
import com.undefined.stellar.sub.StellarSubCommand
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object BrigadierCommandRegistrar {

    private val COMMAND_SOURCE: CommandSourceStack by lazy {
        MinecraftServer.getServer().createCommandSourceStack()
    }

    fun parseAndReturnCancelled(event: PlayerCommandPreprocessEvent): Boolean {
        println("parse called 2")
        val input = event.message.removePrefix("/")
        val results = commandDispatcher().parse(input, COMMAND_SOURCE)
        val context = results.context.build(input)

        if (results.reader.remainingLength == 0) return false

        val baseCommand: StellarCommand = StellarCommands.getStellarCommand(input.substring(input.indexOf('/') + 1, input.indexOf(' '))) ?: return false
        val subCommand = getSubCommands(baseCommand, context).lastOrNull()
        subCommand?.let {
            handleFailureMessageAndExecutions(event.player, subCommand, input)
            if (subCommand.hideDefaultFailureMessages.hide) return true
            println(2)
        } ?: run {
            handleFailureMessageAndExecutions(event.player, baseCommand, input)
            if (baseCommand.hideDefaultFailureMessages.hide) return true
            println(3)
        }
        println(4)

        return baseCommand.hasGlobalHiddenDefaultFailureMessages()
    }

    private fun getSubCommands(
        baseCommand: StellarCommand,
        context: CommandContext<CommandSourceStack>,
        currentIndex: Int = 1,
        listOfSubCommands: List<AbstractStellarSubCommand<*>> = emptyList()
    ): List<AbstractStellarSubCommand<*>> {
        if (listOfSubCommands.size == context.nodes.size - 1) return listOfSubCommands
        for (subCommand in baseCommand.subCommands)
            if (subCommand.name == context.nodes[currentIndex].node.name)
                return getSubCommands(baseCommand, context, currentIndex + 1, listOfSubCommands + subCommand)
        return emptyList()
    }

    private fun <T : CommandSender> handleFailureMessageAndExecutions(sender: T, command: AbstractStellarCommand<*>, input: String) {
        for (message in command.failureMessages) sender.sendRichMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message))
        println("HI")
        for (message in command.globalFailureMessages) sender.sendRichMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message))
        for (execution in command.failureExecutions) execution.run(sender, input)
        println(1)
    }

    fun register(stellarCommand: AbstractStellarCommand<*>) {
        val mainArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(stellarCommand.name)
        mainArgumentBuilder.handleCommand(stellarCommand)
        val node = commandDispatcher().register(mainArgumentBuilder)
        for (alias in stellarCommand.aliases) {
            val aliasCommand = LiteralArgumentBuilder.literal<CommandSourceStack>(alias).redirect(node)
            commandDispatcher().register(aliasCommand)
        }
    }

    private fun AbstractStellarSubCommand<*>.argumentBuilder(alias: String = ""): ArgumentBuilder<CommandSourceStack, *> =
        when (this) {
            is StellarSubCommand -> LiteralArgumentBuilder.literal(alias.ifEmpty { name })
            is BrigadierTypeSubCommand -> {
                ArgumentHelper.nativeSubCommandToArgument(this).handleSuggestions(this)
            }
            else -> throw UnsupportedSubCommandException()
        }

    private fun AbstractStellarSubCommand<*>.handleExecutions(context: CommandContext<CommandSourceStack>) {
        for (subCommand in this.getBase().subCommands) if (!handleSubCommandRunnables(subCommand, context)) return
        when (this) {
            is StellarSubCommand -> for (execution in executions) { execution.run(context.source.bukkitSender) }
            is BrigadierTypeSubCommand -> ArgumentHelper.handleNativeSubCommandExecutors(this, context)
            else -> throw UnsupportedSubCommandException()
        }
    }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleCommand(stellarCommand: AbstractStellarCommand<*>) {
        handleSubCommands(stellarCommand)
        handleRequirements(stellarCommand)
        handleExecutions(stellarCommand)
    }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleAliases(stellarCommand: AbstractStellarCommand<*>) {
        for (alias in stellarCommand.aliases) {
            val childArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(alias)
            childArgumentBuilder.handleCommand(stellarCommand)
            this.then(childArgumentBuilder)
        }
    }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleSubCommands(stellarCommand: AbstractStellarCommand<*>) {
        for (subCommand in stellarCommand.subCommands) {
            val subCommandArgument = subCommand.argumentBuilder()
            subCommandArgument.handleCommand(subCommand)
            this.handleAliases(subCommand)
            then(subCommandArgument)
        }
    }

    private fun RequiredArgumentBuilder<CommandSourceStack, *>.handleSuggestions(command: AbstractStellarSubCommand<*>): RequiredArgumentBuilder<CommandSourceStack, *> {
        if (command.suggestions.isEmpty()) return this
        return suggests { context, suggestionBuilder ->
            for (suggestion in command.suggestions) suggestion.get(context.source.bukkitSender, suggestionBuilder.remaining).forEach { suggestionBuilder.suggest(it) }
            return@suggests suggestionBuilder.buildFuture()
        }
    }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleRequirements(command: AbstractStellarCommand<*>) =
        requires { context ->
            val requirements = command.requirements.all { it.run(context.bukkitSender) }
            val permissionRequirements = command.permissionRequirements.all {
                if (it.permission.isEmpty()) context.hasPermission(it.permissionLevel) else context.hasPermission(it.permissionLevel, it.permission)
            }
            requirements.and(permissionRequirements)
        }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleExecutions(stellarCommand: AbstractStellarCommand<*>) =
        executes { context ->
            for (runnable in stellarCommand.runnables) if (!runnable.run(context.source.bukkitSender)) return@executes 1
            if (stellarCommand is AbstractStellarSubCommand<*>) stellarCommand.handleExecutions(context)
            for (execution in stellarCommand.executions) execution.run(context.source.bukkitSender)
            return@executes Command.SINGLE_SUCCESS
        }

    private fun handleSubCommandRunnables(subCommand: AbstractStellarSubCommand<*>, context: CommandContext<CommandSourceStack>): Boolean {
        when (subCommand) {
            is StellarSubCommand -> subCommand.runnables.forEach { if (!it.run(context.source.bukkitSender)) return false }
            is BrigadierTypeSubCommand -> if (!ArgumentHelper.handleNativeSubCommandRunnables(subCommand, context)) return false
            else -> throw UnsupportedSubCommandException()
        }
        return true
    }

    private fun commandDispatcher(): CommandDispatcher<CommandSourceStack> = (Bukkit.getServer() as CraftServer).server.functions.dispatcher

}