package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.exception.UnsupportedSubCommandException
import com.undefined.stellar.sub.BaseStellarSubCommand
import com.undefined.stellar.sub.StellarSubCommand
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import net.minecraft.commands.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftServer


object BrigadierCommandRegistrar {

    fun register(stellarCommand: BaseStellarCommand<*>) {
        stellarCommand.aliases.plus(stellarCommand.name).forEach { alias ->
            val mainArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(alias)
            mainArgumentBuilder.handleCommand(stellarCommand)
            commandDispatcher().register(mainArgumentBuilder)
        }
    }

    private fun BaseStellarSubCommand<*>.argumentBuilder(alias: String = ""): ArgumentBuilder<CommandSourceStack, *> =
        when (this) {
            is StellarSubCommand -> LiteralArgumentBuilder.literal(alias.ifEmpty { name })
            is NativeTypeSubCommand -> ArgumentHelper.nativeSubCommandToArgument(this).handleSuggestions(this)
            else -> throw UnsupportedSubCommandException()
        }

    private fun BaseStellarSubCommand<*>.handleExecutions(context: CommandContext<CommandSourceStack>) =
        when (this) {
            is NativeTypeSubCommand -> ArgumentHelper.handleNativeSubCommandExecutors(this, context)
            else -> throw UnsupportedSubCommandException()
        }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleCommand(stellarCommand: BaseStellarCommand<*>) {
        handleSubCommands(stellarCommand)
        handleRequirements(stellarCommand)
        handleExecutions(stellarCommand)
    }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleAliases(stellarCommand: BaseStellarCommand<*>) {
        stellarCommand.aliases.forEach { alias ->
            val childArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(alias)
            childArgumentBuilder.handleCommand(stellarCommand)
            this.then(childArgumentBuilder)
        }
    }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleSubCommands(stellarCommand: BaseStellarCommand<*>) {
        stellarCommand.subCommands.forEach { subCommand ->
            val subCommandArgument = subCommand.argumentBuilder()
            subCommandArgument.handleCommand(subCommand)
            this.handleAliases(subCommand)
            then(subCommandArgument)
        }
    }

    private fun RequiredArgumentBuilder<CommandSourceStack, *>.handleSuggestions(command: BaseStellarSubCommand<*>) =
        suggests { context, suggestionBuilder ->
            command.suggestions.forEach { suggestion ->
                suggestion.get(context.source.bukkitSender, suggestionBuilder.remaining).forEach { suggestionBuilder.suggest(it) }
            }
            return@suggests suggestionBuilder.buildFuture()
        }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleRequirements(command: BaseStellarCommand<*>) =
        requires { context ->
            val requirements = command.requirements.all {
                it.run(context.bukkitSender)
            }
            val permissionRequirements = command.permissionRequirements.all {
                if (it.permission.isEmpty()) context.hasPermission(it.permissionLevel) else context.hasPermission(it.permissionLevel, it.permission)
            }
            requirements.and(permissionRequirements)
        }

    private fun ArgumentBuilder<CommandSourceStack, *>.handleExecutions(stellarCommand: BaseStellarCommand<*>) =
        executes { context ->
            if (stellarCommand is BaseStellarSubCommand<*>) stellarCommand.handleExecutions(context)
            stellarCommand.executions.forEach {
                it.run(context.source.bukkitSender)
            }
            return@executes 1
        }

    private fun commandDispatcher(): CommandDispatcher<CommandSourceStack> = (Bukkit.getServer() as CraftServer).server.functions.dispatcher

}