package com.undefined.stellar.v1_16_1

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.types.primitive.PhraseArgument
import net.minecraft.server.v1_16_R1.CommandListenerWrapper

object CommandAdapter {

    fun getBaseCommand(command: AbstractStellarCommand<*>, name: String = command.name): LiteralArgumentBuilder<CommandListenerWrapper> {
        val brigadierCommand = LiteralArgumentBuilder.literal<CommandListenerWrapper>(name)
        handleCommandFunctions(command, brigadierCommand)
        handleArguments(command, brigadierCommand)
        return brigadierCommand
    }

    private fun handleCommandFunctions(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandListenerWrapper, *>) {
        if (command.executions.isNotEmpty() || command.executions.isNotEmpty())
            brigadierCommand.executes { context ->
                sync {
                    BrigadierCommandHelper.handleExecutions(command, context)
                }
                1
            }
        brigadierCommand.requires { source ->
            BrigadierCommandHelper.fulfillsRequirements(command, source)
        }
    }

    private fun handleArguments(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandListenerWrapper, *>) {
        for (argument in command.arguments) {
            when (argument) {
                is LiteralStellarArgument -> handleLiteralArgument(argument, brigadierCommand)
                is PhraseArgument-> handlePhraseArgument(argument, brigadierCommand)
                else -> handleRequiredArgument(argument, brigadierCommand)
            }
        }
    }

    private fun handleLiteralArgument(argument: LiteralStellarArgument, brigadierCommand: ArgumentBuilder<CommandListenerWrapper, *>) {
        for (argumentBuilder in ArgumentHelper.getLiteralArguments(argument)) {
            handleCommandFunctions(argument, argumentBuilder)
            handleArguments(argument, argumentBuilder)
            brigadierCommand.then(argumentBuilder)
        }
    }

    private fun handlePhraseArgument(argument: PhraseArgument, brigadierCommand: ArgumentBuilder<CommandListenerWrapper, *>) {
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        handleCommandFunctions(argument, argumentBuilder)
        handleGreedyStringWordFunctions(argument, argumentBuilder)
        brigadierCommand.then(argumentBuilder)
    }

    private fun handleGreedyStringWordFunctions(argument: PhraseArgument, argumentBuilder: RequiredArgumentBuilder<CommandListenerWrapper, *>) {
        argumentBuilder.executes { context ->
            sync {
                val greedyContext = CommandContextAdapter.getGreedyCommandContext(context)

                for (i in greedyContext.arguments.indices) {
                    val word = argument.words[i] ?: continue
                    for (runnable in word.runnables) runnable(greedyContext)
                    if (i == greedyContext.arguments.lastIndex)
                        for (execution in word.executions) execution(greedyContext)
                }
            }
            Command.SINGLE_SUCCESS
        }

        argumentBuilder.suggests { context, builder ->
            val greedyContext = CommandContextAdapter.getGreedyCommandContext(context)
            var prevChar = ' '
            val input = ArgumentHelper.getArgumentInput(context, argument.name) ?: ""
            val amountOfSpaces: Int = if (input.isEmpty()) 0 else input.count {
                if (prevChar == ' ' && it == ' ') return@count false
                prevChar = it
                it == ' '
            }
            val newBuilder = builder.createOffset(builder.input.lastIndexOf(' ') + 1)
            val word = argument.words[amountOfSpaces] ?: return@suggests newBuilder.buildFuture()
            for (stellarSuggestion in word.suggestions)
                for (suggestion in stellarSuggestion.get(greedyContext))
                    newBuilder.suggest(suggestion.text) { suggestion.tooltip }
            newBuilder.buildFuture()
        }
    }

    private fun handleRequiredArgument(argument: AbstractStellarArgument<*>, brigadierCommand: ArgumentBuilder<CommandListenerWrapper, *>) {
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        handleCommandFunctions(argument, argumentBuilder)
        handleArguments(argument, argumentBuilder)
        brigadierCommand.then(argumentBuilder)
    }

}
