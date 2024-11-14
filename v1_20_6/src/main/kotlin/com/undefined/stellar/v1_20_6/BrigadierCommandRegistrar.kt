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
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.entity.Player

object BrigadierCommandRegistrar {

    private val COMMAND_SOURCE: CommandSourceStack by lazy {
        MinecraftServer.getServer().createCommandSourceStack()
    }

    fun parse(player: Player, input: String) {
        val results = commandDispatcher().parse(input.removePrefix("/"), COMMAND_SOURCE)
        val context = results.context.build(input.removePrefix("/"))

        val baseCommand: StellarCommand = StellarCommands.getStellarCommand(input.substring(input.indexOf('/') + 1, input.indexOf(' '))) ?: return
        val subCommand = getSubCommands(baseCommand, context).last()
        for (message in subCommand.failureMessages)
            player.sendRichMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message))
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

    fun register(stellarCommand: AbstractStellarCommand<*>) {
//        try {
//            val main = LiteralArgumentBuilder.literal<CommandSourceStack>("test")
//            main.then(
//                LiteralArgumentBuilder.literal<CommandSourceStack>("test")
//                    .then(LiteralArgumentBuilder.literal("test"))
//            )
//            commandDispatcher().register(main)
//        } catch (e: CommandSyntaxException) {
//            println("test!")
//            println("Command syntax exception!")
//        }

        val mainArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(stellarCommand.name)
//        mainArgumentBuilder.then(
//            LiteralArgumentBuilder.literal<CommandSourceStack?>("test")
//                .executes { context ->
//                    context.source.player!!.sendSystemMessage(Component.literal("Success!"), true)
//                    Command.SINGLE_SUCCESS
//                }
//        )
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