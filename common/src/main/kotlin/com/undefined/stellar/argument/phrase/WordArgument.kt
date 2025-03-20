package com.undefined.stellar.argument.phrase

import com.undefined.stellar.AbstractStellarArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.phrase.*
import com.undefined.stellar.data.suggestion.*
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

/**
 * An "argument" representing a word inside a [PhraseArgument]. This does not function as a usual argument and does not extend [AbstractStellarArgument].
 */
class WordArgument {

    @ApiStatus.Internal val executions: MutableList<PhraseExecutableExecution<*>> = mutableListOf()
    @ApiStatus.Internal val runnables: MutableList<PhraseExecutableRunnable<*>> = mutableListOf()
    @ApiStatus.Internal val suggestions: MutableList<PhraseExecutableSuggestion<*>> = mutableListOf()

    /**
     * Adds a [Suggestion] on top of the current suggestions.
     * @return The modified [WordArgument] instance.
     */
    fun addSuggestion(suggestion: Suggestion): WordArgument = addSuggestions(suggestion)
    /**
     * Adds a [Suggestion] with the given title and tooltip on top of the current suggestions.
     * @return The modified [WordArgument] instance.
     */
    fun addSuggestion(title: String, tooltip: String? = null): WordArgument = addSuggestions(Suggestion.create(title, tooltip))

    /**
     * Adds a function that returns a list of [Suggestion] on top of the current suggestions. Only works in Kotlin.
     * @return The modified [WordArgument] instance.
     */
    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: PhraseCommandContext<C>.() -> Iterable<Suggestion>): WordArgument = apply {
        suggestions.add(PhraseExecutableSuggestion(C::class) { suggestion(this) })
    }

    /**
     * Adds a function that returns a list of [Suggestion] on top of the current suggestions. Only works in Kotlin.
     * @return The modified [WordArgument] instance.
     */
    fun addSuggestion(suggestion: PhraseCommandContext<CommandSender>.() -> Iterable<Suggestion>): WordArgument = apply {
        suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestion(this) })
    }

    /**
     * Adds multiple [Suggestion] on top of the current suggestions.
     * @return The modified [WordArgument] instance.
     */
    fun addSuggestions(vararg suggestions: Suggestion): WordArgument = apply {
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.toList() })
    }

    /**
     * Adds multiple [Suggestion] with the titles defined in [suggestions] on top of the current suggestions.
     * @return The modified [WordArgument] instance.
     */
    fun addSuggestions(vararg suggestions: String): WordArgument = apply {
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.map { it.toSuggestion() } })
    }

    /**
     * Replaces all suggestions with [suggestions].
     * @return The modified [WordArgument] instance.
     */
    fun setSuggestions(vararg suggestions: Suggestion): WordArgument = apply {
        this.suggestions.clear()
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.toList() })
    }

    /**
     * Replaces all suggestions with [suggestions]. [suggestions] represents a list of [Suggestion] without a tooltip.
     * @return The modified [WordArgument] instance.
     */
    fun setSuggestions(vararg suggestions: String): WordArgument = apply {
        this.suggestions.clear()
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
    }

    /**
     * Replaces all suggestions with [suggestions].
     * @return The modified [WordArgument] instance.
     */
    fun setSuggestions(suggestions: List<Suggestion>): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.toList() })
        return this
    }

    /**
     * Replaces all suggestions with [suggestions]. [suggestions] represents a list of [Suggestion] without a tooltip.
     * @return The modified [WordArgument] instance.
     */
    fun setSuggestionsWithoutTooltip(suggestions: List<String>): WordArgument {
        this.suggestions.clear()
        this.suggestions.add(PhraseExecutableSuggestion(CommandSender::class) { suggestions.map { Suggestion(it, "") } })
        return this
    }

    /**
     * Replaces all executions with [execution]. Only works in Kotlin.
     * @return The modified [WordArgument] instance.
     */
    inline fun <reified C : CommandSender> setExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.clear()
        return addExecution<C>(execution)
    }

    /**
     * Replaces all executions with [execution].
     * @return The modified [WordArgument] instance.
     */
    fun setExecution(execution: PhraseExecution<CommandSender>): WordArgument {
        executions.clear()
        return addExecution(execution)
    }

    /**
     * Adds an execution on top of the other executions. Only works in Kotlin.
     * @return The modified [WordArgument] instance.
     */
    inline fun <reified C : CommandSender> addExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.add(PhraseExecutableExecution(C::class, execution))
        return this
    }

    /**
     * Adds an execution on top of the other executions.
     * @return The modified [WordArgument] instance.
     */
    fun addExecution(execution: PhraseExecution<CommandSender>): WordArgument {
        executions.add(PhraseExecutableExecution(CommandSender::class, execution))
        return this
    }

    /**
     * Adds an async execution on top of the other executions. Only works in Kotlin.
     * @return The modified [WordArgument] instance.
     */
    inline fun <reified C : CommandSender> addAsyncExecution(noinline execution: PhraseCommandContext<C>.() -> Unit): WordArgument {
        executions.add(PhraseExecutableExecution(C::class) { CompletableFuture.supplyAsync { execution(it) } })
        return this
    }

    /**
     * Adds an async execution on top of the other executions.
     * @return The modified [WordArgument] instance.
     */
    fun addAsyncExecution(execution: PhraseExecution<CommandSender>): WordArgument {
        executions.add(PhraseExecutableExecution(CommandSender::class) { CompletableFuture.supplyAsync { execution(it) } })
        return this
    }

    /**
     * Replaces all runnables with [runnable]. Only works in Kotlin.
     * @return The modified [WordArgument] instance.
     */
    inline fun <reified C : CommandSender> setRunnable(noinline runnable: PhraseCommandContext<C>.() -> Boolean): WordArgument {
        runnables.clear()
        return addRunnable<C>(runnable)
    }

    /**
     * Replaces all runnables with [runnable].
     * @return The modified [WordArgument] instance.
     */
    fun setRunnable(runnable: PhraseCommandContext<CommandSender>.() -> Boolean): WordArgument {
        runnables.clear()
        return addRunnable<CommandSender>(runnable)
    }

    /**
     * Adds [runnable] on top of the other runnables. Only works in Kotlin.
     * @return The modified [WordArgument] instance.
     */
    inline fun <reified C : CommandSender> addRunnable(noinline runnable: PhraseCommandContext<C>.() -> Boolean): WordArgument {
        runnables.add(PhraseExecutableRunnable(C::class, runnable))
        return this
    }

    /**
     * Adds [runnable] on top of the other runnables.
     * @return The modified [WordArgument] instance.
     */
    fun addRunnable(runnable: PhraseCommandContext<CommandSender>.() -> Boolean): WordArgument {
        runnables.add(PhraseExecutableRunnable(CommandSender::class, runnable))
        return this
    }

}