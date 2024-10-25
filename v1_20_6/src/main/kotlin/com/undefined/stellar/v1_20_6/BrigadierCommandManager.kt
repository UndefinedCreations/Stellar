package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.undefined.stellar.BaseStellarCommand
import net.minecraft.commands.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftServer

object BrigadierCommandManager {

    fun register(stellarCommand: BaseStellarCommand) {
        stellarCommand.aliases.plus(stellarCommand.name).forEach { commandName ->
            val command = LiteralArgumentBuilder.literal<CommandSourceStack>(commandName).handleCommand(stellarCommand)
            command.handleRequirements(stellarCommand)
            command.handleExecutions(stellarCommand)
            getCommandDispatcher().register(command)
        }
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleCommand(stellarCommand: BaseStellarCommand): LiteralArgumentBuilder<CommandSourceStack> {
        stellarCommand.subCommands.forEach { subCommand ->
            val subCommandArgument = LiteralArgumentBuilder.literal<CommandSourceStack>(subCommand.name)

            subCommandArgument.handleSubCommands(subCommand)
            subCommand.aliases.forEach { alias -> this.then(getSubCommandArgumentBuilder(subCommand, alias)) }
            subCommandArgument.handleRequirements(subCommand)
            subCommandArgument.handleExecutions(subCommand)

            this.then(subCommandArgument)
        }

        return this
    }

    private fun getSubCommandArgumentBuilder(subCommand: BaseStellarCommand, commandName: String): LiteralArgumentBuilder<CommandSourceStack> {
        val subCommandArgumentBuilder = LiteralArgumentBuilder.literal<CommandSourceStack>(commandName)
        subCommandArgumentBuilder.handleSubCommands(subCommand)
        return subCommandArgumentBuilder
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleSubCommands(command: BaseStellarCommand) {
        command.subCommands.forEach { subCommand ->
            val childArgument = LiteralArgumentBuilder.literal<CommandSourceStack>(subCommand.name)
            childArgument.handleRequirements(subCommand)
            childArgument.handleExecutions(subCommand)
            this.then(childArgument.handleCommand(subCommand))
        }
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleRequirements(command: BaseStellarCommand) {
        requires { context -> command.requirements.all { it.run(context.bukkitSender) } }
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.handleExecutions(command: BaseStellarCommand) {
        executes { context ->
            command.executions.forEach { it.run(context.source.bukkitSender) }
            return@executes 1
        }
    }

    private fun getCommandDispatcher(): CommandDispatcher<CommandSourceStack> = (Bukkit.getServer() as CraftServer).server.functions.dispatcher

}