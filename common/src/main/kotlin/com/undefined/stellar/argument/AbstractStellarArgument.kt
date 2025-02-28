package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarArgument<T, R>(val parent: AbstractStellarCommand<*>, name: String) : AbstractStellarCommand<T>(name) {

    @ApiStatus.Internal open val suggestions: MutableList<StellarSuggestion<*>> = mutableListOf()
    override val registerExecutions: MutableList<() -> Unit>
        get() = base.registerExecutions

    fun addSuggestion(title: String, tooltip: String): T {
        addSuggestion(Suggestion(title, tooltip))
        return this as T
    }

    fun addSuggestion(text: String): T = addSuggestion(text, "")

    fun addSuggestion(suggestion: Suggestion): T = addSuggestions(listOf(suggestion))

    fun addSuggestions(suggestions: List<Suggestion>): T {
        this.suggestions.add(StellarSuggestion(CommandSender::class) { CompletableFuture.completedFuture(suggestions) })
        return this as T
    }

    fun addSuggestions(vararg suggestions: Suggestion): T = addSuggestions(suggestions.toList())
    fun addSuggestions(vararg suggestions: String): T = addSuggestions(suggestions.map { Suggestion(it, "") })

    fun setSuggestions(vararg suggestions: Suggestion): T {
        this.suggestions.clear()
        return addSuggestions(suggestions.toList())
    }

    fun setSuggestions(vararg suggestions: String): T {
        this.suggestions.clear()
        return addSuggestions(suggestions.map { Suggestion(it, "") })
    }

    fun setSuggestions(suggestions: List<Suggestion>): T {
        this.suggestions.clear()
        return addSuggestions(suggestions.toList())
    }

    fun setSuggestionsWithoutTooltip(suggestions: List<String>): T {
        this.suggestions.clear()
        return addSuggestions(suggestions.map { Suggestion(it, "") })
    }

    inline fun <reified C : CommandSender> addFutureSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> CompletableFuture<Iterable<Suggestion>>): T {
        suggestions.add(StellarSuggestion(C::class, suggestion))
        return this as T
    }

    inline fun <reified C : CommandSender> addAsyncSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> List<Suggestion>): T {
        suggestions.add(StellarSuggestion(C::class) { CompletableFuture.supplyAsync { suggestion(this, it) } })
        return this as T
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> List<Suggestion>): T {
        suggestions.add(StellarSuggestion(C::class) { CompletableFuture.completedFuture(suggestion(this, it)) })
        return this as T
    }

    override val base: AbstractStellarCommand<*>
        get() = parent.base
    override fun register(plugin: JavaPlugin) = parent.register(plugin)

}