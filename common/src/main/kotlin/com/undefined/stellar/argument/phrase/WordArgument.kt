package com.undefined.stellar.argument.phrase

import com.undefined.stellar.data.phrase.*
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

class WordArgument {

    @ApiStatus.Internal val executions: MutableList<PhraseExecutableExecution<*>> = mutableListOf()
    @ApiStatus.Internal val runnables: MutableList<PhraseExecutableRunnable<*>> = mutableListOf()
    @ApiStatus.Internal val suggestions: MutableList<PhraseExecutableSuggestion<*>> = mutableListOf()

    fun addSuggestion(text: String, tooltip: String): WordArgument {
        addSuggestion(Suggestion(text, tooltip))
        return this
    }

    fun addSuggestion(suggestion: Suggestion): WordArgument {
        suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { listOf(suggestion) })
        return this
    }

    fun addSuggestions(list: List<Suggestion>): WordArgument {
        suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { list })
        return this
    }

    fun addSuggestions(vararg list: Suggestion): WordArgument {
        suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { list.toList() })
        return this
    }

    fun addSuggestionsWithoutWordArgumentTooltip(list: List<String>): WordArgument {
        suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { list.map { Suggestion(it, "") } })
        return this
    }

    fun addSuggestions(vararg suggestions: String): WordArgument {
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    fun setSuggestions(vararg suggestions: Suggestion): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.toList() })
        return this
    }

    fun setSuggestions(vararg suggestions: String): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    fun setSuggestions(suggestions: List<Suggestion>): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.toList() })
        return this
    }

    fun setSuggestionsWithoutTooltip(suggestions: List<String>): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: PhraseCommandContext<C>.() -> List<Suggestion>): WordArgument {
        suggestions.add(PhraseExecutableSuggestion(C::class, suggestion))
        return this
    }

    fun addSuggestion(suggestion: PhraseCommandContext<CommandSender>.() -> List<Suggestion>): WordArgument {
        suggestions.add(PhraseExecutableSuggestion(CommandSender::class, suggestion))
        return this
    }

    inline fun <reified C : CommandSender> setExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.clear()
        return addExecution<C>(execution)
    }

    fun setExecution(execution: PhraseExecution<CommandSender>): WordArgument {
        executions.clear()
        return addExecution(execution)
    }

    inline fun <reified C : CommandSender> addExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.add(PhraseExecutableExecution(C::class, execution))
        return this
    }

    fun addExecution(execution: PhraseExecution<CommandSender>): WordArgument {
        executions.add(PhraseExecutableExecution(CommandSender::class, execution))
        return this
    }

    inline fun <reified C : CommandSender> addAsyncExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.add(PhraseExecutableExecution(C::class) { CompletableFuture.supplyAsync { execution(it) } })
        return this
    }

    fun addAsyncExecution(execution: PhraseExecution<CommandSender>): WordArgument {
        executions.add(PhraseExecutableExecution(CommandSender::class) { CompletableFuture.supplyAsync { execution(it) } })
        return this
    }

    inline fun <reified C : CommandSender> setRunnable(noinline runnable: PhraseCommandContext<C>.() -> Boolean): WordArgument {
        runnables.clear()
        return addRunnable<C>(runnable)
    }

    fun setRunnable(runnable: PhraseCommandContext<CommandSender>.() -> Boolean): WordArgument {
        runnables.clear()
        return addRunnable<CommandSender>(runnable)
    }

    inline fun <reified C : CommandSender> addRunnable(noinline runnable: PhraseCommandContext<C>.() -> Boolean): WordArgument {
        runnables.add(PhraseExecutableRunnable(C::class, runnable))
        return this
    }

    fun addRunnable(runnable: PhraseCommandContext<CommandSender>.() -> Boolean): WordArgument {
        runnables.add(PhraseExecutableRunnable(CommandSender::class, runnable))
        return this
    }

}