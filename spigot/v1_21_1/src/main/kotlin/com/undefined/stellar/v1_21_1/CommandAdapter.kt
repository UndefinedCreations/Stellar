package com.undefined.stellar.v1_21_1

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.primitive.PhraseArgument
import com.undefined.stellar.data.suggestion.Suggestion
import net.minecraft.commands.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.CompletableFuture

object CommandAdapter {

    fun getBaseCommand(command: AbstractStellarCommand<*>, name: String = command.name): LiteralArgumentBuilder<CommandSourceStack> {
        val brigadierCommand = LiteralArgumentBuilder.literal<CommandSourceStack>(name)
        handleCommandFunctions(command, brigadierCommand)
        handleArguments(command, brigadierCommand)
        return brigadierCommand
    }

    fun handleCommandFunctions(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        if (command.executions.isNotEmpty() || command.executions.isNotEmpty())
            brigadierCommand.executes { context ->
                object : BukkitRunnable() {
                    override fun run() =  BrigadierCommandHelper.handleExecutions(command, context)
                }.runTask(CommandRegistrar.plugin)
                1
            }
        brigadierCommand.requires { source ->
            BrigadierCommandHelper.fulfillsRequirements(command, source)
        }

        if (command !is AbstractStellarArgument || command.suggestions.isEmpty() || brigadierCommand !is RequiredArgumentBuilder<CommandSourceStack, *>) return
        brigadierCommand.suggests { context, builder ->
            BrigadierCommandHelper.handleSuggestions(command, context, builder)
        }
    }

    fun handleArguments(command: AbstractStellarCommand<*>, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        for (argument in command.arguments) {
            when (argument) {
                is LiteralStellarArgument -> handleLiteralArgument(argument, brigadierCommand)
                is PhraseArgument-> handlePhraseArgument(argument, brigadierCommand)
                else -> handleRequiredArgument(argument, brigadierCommand)
            }
        }
    }

    fun getMojangSuggestions(builder: SuggestionsBuilder, suggestionsFuture: CompletableFuture<List<Suggestion>>): CompletableFuture<Suggestions> {
        val range = StringRange.between(builder.start, builder.input.length)
        val future = suggestionsFuture.thenApplyAsync { suggestions ->
            Suggestions(range, suggestions.map { suggestion ->
                com.mojang.brigadier.suggestion.Suggestion(range, suggestion.text) { suggestion.tooltip }
            })
        }
        return future
    }

    private fun handleLiteralArgument(argument: LiteralStellarArgument, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        for (argumentBuilder in ArgumentHelper.getLiteralArguments(argument)) {
            handleCommandFunctions(argument, argumentBuilder)
            handleArguments(argument, argumentBuilder)
            brigadierCommand.then(argumentBuilder)
        }
    }

    private fun handlePhraseArgument(argument: PhraseArgument, brigadierCommand: ArgumentBuilder<CommandSourceStack, *>) {
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        handleCommandFunctions(argument, argumentBuilder)
        handleGreedyStringWordFunctions(argument, argumentBuilder)
        brigadierCommand.then(argumentBuilder)
    }

    private fun handleGreedyStringWordFunctions(argument: PhraseArgument, argumentBuilder: RequiredArgumentBuilder<CommandSourceStack, *>) {
        if (argument.executions.isNotEmpty() || argument.executions.isNotEmpty()) argumentBuilder.executes { context ->
            Bukkit.getScheduler().runTask(CommandRegistrar.plugin, Runnable {
                val greedyContext = CommandContextAdapter.getGreedyCommandContext(context)

                for (i in greedyContext.arguments.indices) {
                    val word = argument.words[i] ?: continue
                    for (runnable in word.runnables) runnable(greedyContext)
                    if (i == greedyContext.arguments.lastIndex)
                        for (execution in word.executions) execution(greedyContext)
                }
            })
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
        val argumentBuilder = ArgumentHelper.getRequiredArgumentBuilder(argument)
        handleCommandFunctions(argument, argumentBuilder)
        handleArguments(argument, argumentBuilder)
        brigadierCommand.then(argumentBuilder)
    }

}
