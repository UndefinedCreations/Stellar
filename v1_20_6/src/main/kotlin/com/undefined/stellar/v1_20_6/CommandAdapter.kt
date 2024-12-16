package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.types.custom.ListArgument
import com.undefined.stellar.argument.types.primitive.GreedyStringArgument
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

        println("1!\n")
        if (command !is AbstractStellarArgument || command.suggestions.isEmpty() || brigadierCommand !is RequiredArgumentBuilder<CommandSourceStack, *>) return
        println("suggestions!\n")
        brigadierCommand.suggests { context, builder ->
            BrigadierCommandHelper.handleSuggestions(command, context, builder)
        }
    }

    fun handleArguments(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        for (argument in command.arguments) {
            when (argument) {
                is LiteralStellarArgument -> handleLiteralArgument(argument, brigadierCommand)
                is GreedyStringArgument-> handleGreedyStringArgument(argument, brigadierCommand)
                else -> handleRequiredArgument(argument, brigadierCommand)
            }
        }
    }

    private fun handleLiteralArgument(argument: LiteralStellarArgument, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        for (argumentBuilder in ArgumentHelper.getLiteralArguments(argument)) {
            handleCommandFunctions(argument, argumentBuilder)
            handleArguments(argument, argumentBuilder)
            brigadierCommand.then(argumentBuilder)
        }
    }

    private fun handleGreedyStringArgument(argument: GreedyStringArgument, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        handleCommandFunctions(argument, argumentBuilder)
        handleGreedyStringWordFunctions(argument, argumentBuilder)
        brigadierCommand.then(argumentBuilder)
    }

    private fun handleGreedyStringWordFunctions(argument: GreedyStringArgument, argumentBuilder: ArgumentBuilder<CommandSourceStack, *>) {
        argumentBuilder.executes { context ->
            val greedyContext = CommandContextAdapter.getGreedyCommandContext(context)

            for (i in greedyContext.arguments.indices) {
                val word = argument.words[i] ?: continue
                for (runnable in word.runnables) runnable(greedyContext)
                if (i == greedyContext.arguments.lastIndex)
                    for (execution in word.executions) execution(greedyContext)
            }
            Command.SINGLE_SUCCESS
        }
    }

    private fun handleRequiredArgument(argument: AbstractStellarArgument<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        handleCommandFunctions(argument, argumentBuilder)
        handleArguments(argument, argumentBuilder)
        brigadierCommand.then(argumentBuilder)
    }

}
