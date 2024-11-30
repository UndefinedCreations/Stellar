package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.StellarCommands
import com.undefined.stellar.data.help.CustomCommandHelpTopic
import com.undefined.stellar.exception.UnsupportedSubCommandException
import com.undefined.stellar.sub.AbstractStellarSubCommand
import com.undefined.stellar.sub.BaseStellarSubCommand
import com.undefined.stellar.sub.StellarSubCommand
import com.undefined.stellar.sub.custom.CustomSubCommand
import com.undefined.stellar.sub.custom.CustomSubCommandInfo
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.help.CustomHelpTopic
import org.bukkit.help.GenericCommandHelpTopic
import org.bukkit.help.HelpTopic
import java.util.SortedMap

object BrigadierCommandRegistrar {

    private val COMMAND_SOURCE: CommandSourceStack by lazy {
        MinecraftServer.getServer().createCommandSourceStack()
    }

    fun parseAndReturnCancelled(sender: CommandSender, input: String): Boolean {
        val results = commandDispatcher().parse(input, COMMAND_SOURCE)
        val context = results.context.build(input)

        if (results.reader.remainingLength == 0) return false

        val baseCommand: AbstractStellarCommand<*> = StellarCommands.getStellarCommand(input.substring(input.indexOf('/') + 1, input.indexOf(' '))) ?: return false
        val subCommand = getSubCommands(baseCommand, context).lastOrNull()
        subCommand?.let {
            handleFailureMessageAndExecutions(sender, subCommand, input)
            if (subCommand.hideDefaultFailureMessages.hide) return true
        } ?: run {
            handleFailureMessageAndExecutions(sender, baseCommand, input)
            if (baseCommand.hideDefaultFailureMessages.hide) return true
        }
        if (subCommand is CustomSubCommand<*>) subCommand.failureExecution(CustomSubCommandInfo(sender, input, subCommand.parse(sender, input)))

        return baseCommand.hasGlobalHiddenDefaultFailureMessages()
    }

    private fun getSubCommands(
        baseCommand: AbstractStellarCommand<*>,
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
        for (message in command.failureMessages) sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message))
        for (message in command.globalFailureMessages) sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message))
        for (execution in command.failureExecutions) execution.run(sender, input)
    }

    fun register(stellarCommand: AbstractStellarCommand<*>) {
//        val helpTopic = object : HelpTopic() {
//            init {
//                name = "test"
//                shortText = "shortText"
//                fullText = "fullText"
//            }
//            override fun canSee(player: CommandSender): Boolean = true
//        }
        val information: SortedMap<String, String> = sortedMapOf()
        if (stellarCommand.description != "") information["Description"] = stellarCommand.description
        if (stellarCommand.usage != "") information["Usage"] = stellarCommand.usage
        if (stellarCommand.aliases.isNotEmpty()) information["Aliases"] = stellarCommand.aliases.joinToString(", ")
        val helpTopic = CustomCommandHelpTopic(stellarCommand.name, stellarCommand.description, information) {
            val context = MinecraftServer.getServer().createCommandSourceStack()
            val requirements = stellarCommand.requirements.all { it.run(this) }
            val permissionRequirements = stellarCommand.permissionRequirements.all {
                if (it.permission.isEmpty()) context.hasPermission(it.permissionLevel) else context.hasPermission(
                    it.permissionLevel,
                    it.permission
                )
            }
            requirements.and(permissionRequirements)
        }
        Bukkit.getServer().helpMap.addTopic(helpTopic)

        val mainArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(stellarCommand.name)
        mainArgumentBuilder.handleCommand(stellarCommand)
        val node = commandDispatcher().register(mainArgumentBuilder)
        for (alias in stellarCommand.aliases) {
            val aliasCommand = LiteralArgumentBuilder.literal<CommandSourceStack>(alias).redirect(node)
            commandDispatcher().register(aliasCommand)
        }
    }

    private fun getArgumentBuilderFromSubCommand(subCommand: AbstractStellarSubCommand<*>, alias: String = ""): ArgumentBuilder<CommandSourceStack, *> =
        when (subCommand) {
            is StellarSubCommand -> LiteralArgumentBuilder.literal(alias.ifEmpty { subCommand.name })
            is BaseStellarSubCommand -> {
                ArgumentHelper.nativeSubCommandToArgument(subCommand).handleSuggestions(subCommand)
            }
            else -> throw UnsupportedSubCommandException()
        }

    private fun BaseStellarSubCommand<*>.handleExecutions(context: CommandContext<CommandSourceStack>) {
        for (subCommand in this.getBase().subCommands) if (subCommand is BaseStellarSubCommand && !handleSubCommandRunnables(subCommand, context)) return
        when (this) {
            is StellarSubCommand -> for (execution in executions) { execution.run(context.source.bukkitSender) }
            is BaseStellarSubCommand -> ArgumentHelper.handleNativeSubCommandExecutors(this, context)
            else -> throw UnsupportedSubCommandException()
        }
    }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleCommand(stellarCommand: AbstractStellarCommand<*>) {
        handleSubCommands(stellarCommand)
        if (stellarCommand !is CustomSubCommand<*>) handleRequirements(stellarCommand)
        if (stellarCommand !is CustomSubCommand<*>) handleExecutions(stellarCommand)
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
            if (subCommand is CustomSubCommand<*>) {
                val type = ArgumentHelper.getArgumentTypeFromBrigadierSubCommand(subCommand.type)
                val argument: RequiredArgumentBuilder<CommandSourceStack, *> = RequiredArgumentBuilder.argument(subCommand.name, type)
                argument.requires { subCommand.requirement() }
                argument.suggests { context, builder ->
                    subCommand.listSuggestions(context.source.bukkitSender).forEach { builder.suggest(it) }
                    builder.buildFuture()
                }
                then(argument)
                return
            }
            val subCommandArgument = getArgumentBuilderFromSubCommand(subCommand)
            subCommandArgument.handleCommand(subCommand)
            this.handleAliases(subCommand)
            then(subCommandArgument)
        }
    }

    private fun RequiredArgumentBuilder<CommandSourceStack, *>.handleSuggestions(command: BaseStellarSubCommand<*>): RequiredArgumentBuilder<CommandSourceStack, *> {
        if (command.suggestions.isEmpty()) return this
        return suggests { context, suggestionBuilder ->
            for (suggestion in command.suggestions) { suggestion.get(context.source.bukkitSender, suggestionBuilder.remaining).forEach { suggestionBuilder.suggest(it) } }
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
            if (stellarCommand is BaseStellarSubCommand<*>) stellarCommand.handleExecutions(context)
            for (execution in stellarCommand.executions) execution.run(context.source.bukkitSender)
            return@executes Command.SINGLE_SUCCESS
        }

    private fun handleSubCommandRunnables(subCommand: BaseStellarSubCommand<*>, context: CommandContext<CommandSourceStack>): Boolean {
        subCommand.runnables.forEach { if (!it.run(context.source.bukkitSender)) return false }
        return ArgumentHelper.handleNativeSubCommandRunnables(subCommand, context)
    }

    private fun commandDispatcher(): CommandDispatcher<CommandSourceStack> = (Bukkit.getServer() as CraftServer).server.functions.dispatcher

}