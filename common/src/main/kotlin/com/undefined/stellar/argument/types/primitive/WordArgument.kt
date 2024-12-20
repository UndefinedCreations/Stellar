package com.undefined.stellar.argument.types.primitive

import com.undefined.stellar.data.argument.PhraseCommandContext
import com.undefined.stellar.data.execution.PhraseStellarExecution
import com.undefined.stellar.data.execution.PhraseStellarRunnable
import com.undefined.stellar.data.suggestion.PhraseStellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus

class WordArgument {

    @ApiStatus.Internal val executions: MutableList<PhraseStellarExecution<*>> = mutableListOf()
    @ApiStatus.Internal val runnables: MutableList<PhraseStellarRunnable<*>> = mutableListOf()
    @ApiStatus.Internal val suggestions: MutableList<PhraseStellarSuggestion<*>> = mutableListOf()

    fun addSuggestion(text: String, tooltip: String): WordArgument {
        addSuggestion(Suggestion(text, tooltip))
        return this
    }

    fun addSuggestion(suggestion: Suggestion): WordArgument {
        suggestions.add(PhraseStellarSuggestion(CommandSender::class) { listOf(suggestion) })
        return this
    }

    fun addSuggestions(list: List<Suggestion>): WordArgument {
        suggestions.add(PhraseStellarSuggestion(CommandSender::class) { list })
        return this
    }

    fun addSuggestions(vararg list: Suggestion): WordArgument {
        suggestions.add(PhraseStellarSuggestion(CommandSender::class) { list.toList() })
        return this
    }

    fun addSuggestionsWithoutWordArgumentTooltip(list: List<String>): WordArgument {
        suggestions.add(PhraseStellarSuggestion(CommandSender::class) { list.map { Suggestion(it, "") } })
        return this
    }

    fun addSuggestions(vararg suggestions: String): WordArgument {
        this.suggestions.add(PhraseStellarSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    fun setSuggestions(vararg suggestions: Suggestion): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseStellarSuggestion(CommandSender::class) { suggestions.toList() })
        return this
    }

    fun setSuggestions(vararg suggestions: String): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseStellarSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    fun setSuggestions(suggestions: List<Suggestion>): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseStellarSuggestion(CommandSender::class) { suggestions.toList() })
        return this
    }

    fun setSuggestionsWithoutTooltip(suggestions: List<String>): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseStellarSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: PhraseCommandContext<C>.() -> List<Suggestion>): WordArgument {
        suggestions.add(PhraseStellarSuggestion(C::class, suggestion))
        return this
    }

    inline fun <reified C : CommandSender> setExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.clear()
        return addExecution<C>(execution)
    }

    inline fun <reified C : CommandSender> addExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.add(PhraseStellarExecution(C::class, execution))
        return this
    }

    inline fun <reified C : CommandSender> setRunnable(noinline runnable: PhraseCommandContext<C>.() -> Boolean): WordArgument {
        runnables.clear()
        return addRunnable<C>(runnable)
    }

    inline fun <reified C : CommandSender> addRunnable(noinline runnable: PhraseCommandContext<C>.() -> Boolean): WordArgument {
        runnables.add(PhraseStellarRunnable(C::class, runnable))
        return this
    }

}