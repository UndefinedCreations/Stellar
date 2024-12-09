package com.undefined.stellar.argument.types.primitive

import com.undefined.stellar.data.argument.GreedyCommandContext
import com.undefined.stellar.data.execution.GreedyStellarExecution
import com.undefined.stellar.data.execution.GreedyStellarRunnable
import com.undefined.stellar.data.suggestion.GreedyStellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus

class GreedyWordArgument {

    @ApiStatus.Internal val executions: MutableList<GreedyStellarExecution<*>> = mutableListOf()
    @ApiStatus.Internal val runnables: MutableList<GreedyStellarRunnable<*>> = mutableListOf()
    @ApiStatus.Internal val suggestions: MutableList<GreedyStellarSuggestion<*>> = mutableListOf()

    fun addSuggestion(text: String, tooltip: String): GreedyWordArgument {
        addSuggestion(Suggestion(text, tooltip))
        return this
    }

    fun addSuggestion(suggestion: Suggestion): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion<CommandSender> { listOf(suggestion) })
        return this
    }

    fun addSuggestions(list: List<Suggestion>): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion<CommandSender> { list })
        return this
    }

    fun addSuggestions(vararg list: Suggestion): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion<CommandSender> { list.toList() })
        return this
    }

    fun addSuggestionsWithoutGreedyWordArgumentooltip(list: List<String>): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion<CommandSender> { list.map { Suggestion(it, "") } })
        return this
    }

    fun addSuggestions(vararg list: String): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion<CommandSender> { list.map { Suggestion(it, "") } })
        return this
    }

    fun setSuggestions(vararg suggestion: Suggestion): GreedyWordArgument {
        suggestions.clear()
        suggestions.add(GreedyStellarSuggestion<CommandSender> { suggestion.toList() })
        return this
    }

    fun setSuggestions(vararg suggestion: String): GreedyWordArgument {
        suggestions.clear()
        suggestions.add(GreedyStellarSuggestion<CommandSender> { suggestion.map { Suggestion(it, "") } })
        return this
    }

    fun setSuggestions(suggestion: List<Suggestion>): GreedyWordArgument {
        suggestions.clear()
        suggestions.add(GreedyStellarSuggestion<CommandSender> { suggestion.toList() })
        return this
    }

    fun setSuggestionsWithoutTooltip(suggestion: List<String>): GreedyWordArgument {
        suggestions.clear()
        suggestions.add(GreedyStellarSuggestion<CommandSender> { suggestion.map { Suggestion(it, "") } })
        return this
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: GreedyCommandContext<C>.() -> List<Suggestion>): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion(suggestion))
        return this
    }

    inline fun <reified C : CommandSender> setExecution(noinline execution: GreedyCommandContext<C>.() -> Unit): GreedyWordArgument {
        executions.clear()
        return addExecution<C>(execution)
    }

    inline fun <reified C : CommandSender> addExecution(noinline execution: GreedyCommandContext<C>.() -> Unit): GreedyWordArgument {
        executions.add(GreedyStellarExecution(execution))
        return this
    }

    inline fun <reified C : CommandSender> setRunnable(noinline runnable: GreedyCommandContext<C>.() -> Boolean): GreedyWordArgument {
        runnables.clear()
        return addRunnable<C>(runnable)
    }

    inline fun <reified C : CommandSender> addRunnable(noinline runnable: GreedyCommandContext<C>.() -> Boolean): GreedyWordArgument {
        runnables.add(GreedyStellarRunnable(runnable))
        return this
    }

}