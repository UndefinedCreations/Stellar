package com.undefined.stellar.argument.phrase

import com.undefined.stellar.data.phrase.PhraseCommandContext
import com.undefined.stellar.data.phrase.PhraseExecution
import com.undefined.stellar.data.phrase.PhraseRunnable
import com.undefined.stellar.data.phrase.PhraseStellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

class WordArgument {

    @ApiStatus.Internal val executions: MutableList<PhraseExecution<*>> = mutableListOf()
    @ApiStatus.Internal val runnables: MutableList<PhraseRunnable<*>> = mutableListOf()
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
        executions.add(PhraseExecution(C::class, execution))
        return this
    }

    inline fun <reified C : CommandSender> addAsyncExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.add(PhraseExecution(C::class) { CompletableFuture.supplyAsync { execution() } })
        return this
    }

    inline fun <reified C : CommandSender> setRunnable(noinline runnable: PhraseCommandContext<C>.() -> Boolean): WordArgument {
        runnables.clear()
        return addRunnable<C>(runnable)
    }

    inline fun <reified C : CommandSender> addRunnable(noinline runnable: PhraseCommandContext<C>.() -> Boolean): WordArgument {
        runnables.add(PhraseRunnable(C::class, runnable))
        return this
    }

}