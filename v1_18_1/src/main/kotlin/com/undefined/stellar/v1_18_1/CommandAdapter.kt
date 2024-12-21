package com.undefined.stellar.v1_18_1

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.types.primitive.PhraseArgument
import net.minecraft.commands.CommandSourceStack

object CommandAdapter {

    fun getBaseCommand(command: AbstractStellarCommand<*>, name: String = command.name): LiteralArgumentBuilder<CommandSourceStack> {
        println(4)
        val brigadierCommand = LiteralArgumentBuilder.literal<CommandSourceStack>(name)
        println(5)
        handleCommandFunctions(command, brigadierCommand)
        println(6)
        handleArguments(command, brigadierCommand)
        println(7)
        return brigadierCommand
    }

    fun handleCommandFunctions(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        println(8)
        if (command.executions.isNotEmpty() || command.executions.isNotEmpty())
            println(9)
            brigadierCommand.executes { context ->
                println(10)
                BrigadierCommandHelper.handleExecutions(command, context)
                1
            }
        brigadierCommand.requires { source ->
            println(11)
            BrigadierCommandHelper.fulfillsRequirements(command, source)
        }
    }

    fun handleArguments(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        println(12)
        for (argument in command.arguments) {
            println(13)
            when (argument) {
                is LiteralStellarArgument -> handleLiteralArgument(argument, brigadierCommand)
                is PhraseArgument-> handlePhraseArgument(argument, brigadierCommand)
                else -> handleRequiredArgument(argument, brigadierCommand)
            }
        }
    }

    private fun handleLiteralArgument(argument: LiteralStellarArgument, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        println(14)
        for (argumentBuilder in ArgumentHelper.getLiteralArguments(argument)) {
            println(15)
            handleCommandFunctions(argument, argumentBuilder)
            println(16)
            handleArguments(argument, argumentBuilder)
            println(17)
            brigadierCommand.then(argumentBuilder)
            println(18)
        }
    }

    private fun handlePhraseArgument(argument: PhraseArgument, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        handleCommandFunctions(argument, argumentBuilder)
        handleGreedyStringWordFunctions(argument, argumentBuilder)
        brigadierCommand.then(argumentBuilder)
    }

    private fun handleGreedyStringWordFunctions(argument: PhraseArgument, argumentBuilder: RequiredArgumentBuilder<CommandSourceStack, *>) {
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

    private fun handleRequiredArgument(argument: AbstractStellarArgument<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        println(19)
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        println(20)
        handleCommandFunctions(argument, argumentBuilder)
        println(21)
        handleArguments(argument, argumentBuilder)
        println(22)
        brigadierCommand.then(argumentBuilder)
        println(23)
    }

}
