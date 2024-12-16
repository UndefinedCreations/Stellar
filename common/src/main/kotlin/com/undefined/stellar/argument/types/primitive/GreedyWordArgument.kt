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
        suggestions.add(GreedyStellarSuggestion(CommandSender::class) { listOf(suggestion) })
        return this
    }

    fun addSuggestions(list: List<Suggestion>): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion(CommandSender::class) { list })
        return this
    }

    fun addSuggestions(vararg list: Suggestion): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion(CommandSender::class) { list.toList() })
        return this
    }

    fun addSuggestionsWithoutGreedyWordArgumentTooltip(list: List<String>): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion(CommandSender::class) { list.map { Suggestion(it, "") } })
        return this
    }

    fun addSuggestions(vararg suggestions: String): GreedyWordArgument {
        this.suggestions.add(GreedyStellarSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    fun setSuggestions(vararg suggestions: Suggestion): GreedyWordArgument {
        this.suggestions.clear()
        this.suggestions.add(GreedyStellarSuggestion(CommandSender::class) { suggestions.toList() })
        return this
    }

    fun setSuggestions(vararg suggestions: String): GreedyWordArgument {
        this.suggestions.clear()
        this.suggestions.add(GreedyStellarSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    fun setSuggestions(suggestions: List<Suggestion>): GreedyWordArgument {
        this.suggestions.clear()
        this.suggestions.add(GreedyStellarSuggestion(CommandSender::class) { suggestions.toList() })
        return this
    }

    fun setSuggestionsWithoutTooltip(suggestions: List<String>): GreedyWordArgument {
        this.suggestions.clear()
        this.suggestions.add(GreedyStellarSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: GreedyCommandContext<C>.() -> List<Suggestion>): GreedyWordArgument {
        suggestions.add(GreedyStellarSuggestion(C::class, suggestion))
        return this
    }

    inline fun <reified C : CommandSender> setExecution(noinline execution: GreedyCommandContext<C>.() -> Unit): GreedyWordArgument {
        executions.clear()
        return addExecution<C>(execution)
    }

    inline fun <reified C : CommandSender> addExecution(noinline execution: GreedyCommandContext<C>.() -> Unit): GreedyWordArgument {
        executions.add(GreedyStellarExecution(C::class, execution))
        return this
    }

    inline fun <reified C : CommandSender> setRunnable(noinline runnable: GreedyCommandContext<C>.() -> Boolean): GreedyWordArgument {
        runnables.clear()
        return addRunnable<C>(runnable)
    }

    inline fun <reified C : CommandSender> addRunnable(noinline runnable: GreedyCommandContext<C>.() -> Boolean): GreedyWordArgument {
        runnables.add(GreedyStellarRunnable(C::class, runnable))
        return this
    }

}