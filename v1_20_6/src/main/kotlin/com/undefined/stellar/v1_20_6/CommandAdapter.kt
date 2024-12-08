package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand
import com.undefined.stellar.sub.LiteralStellarSubCommand
import net.minecraft.commands.CommandSourceStack

object CommandAdapter {

    fun getBaseCommand(command: AbstractStellarCommand<*>, name: String = command.name): LiteralArgumentBuilder<CommandSourceStack> {
        val brigadierCommand = LiteralArgumentBuilder.literal<CommandSourceStack>(name)
        handleCommandFunctions(command, brigadierCommand)
        handleArguments(command, brigadierCommand)
        return brigadierCommand
    }

    fun handleCommandFunctions(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        brigadierCommand.executes { context ->
            BrigadierCommandHelper.handleExecutions(command, context)
            1
        }
        brigadierCommand.requires { source ->
            BrigadierCommandHelper.fulfillsRequirements(command, source)
        }
    }

    fun handleArguments(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        for (argument in command.subCommands) {
            if (argument is LiteralStellarSubCommand) {
                handleLiteralArgument(argument, brigadierCommand)
                continue
            }
            handleRequiredArgument(argument, brigadierCommand)
        }
    }

    private fun handleLiteralArgument(argument: AbstractStellarSubCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        for (argumentBuilder in ArgumentHelper.getLiteralArguments(argument)) {
            handleCommandFunctions(argument, argumentBuilder)
            handleArguments(argument, argumentBuilder)
            brigadierCommand.then(argumentBuilder)
        }
    }

    private fun handleRequiredArgument(argument: AbstractStellarSubCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        handleCommandFunctions(argument, argumentBuilder)
        handleArguments(argument, argumentBuilder)
        brigadierCommand.then(argumentBuilder)
    }

}
