package com.undefined.stellar

import com.mojang.brigadier.arguments.ArgumentType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

/**
 * Being an extension of [AbstractStellarCommand], it represents an argument.
 *
 * @param name The name of the argument.
 * @param argumentType This is used internally and should not be used by the end user. Is used when the Brigadier library contains the argument type wanted.
 */
@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarArgument<T : AbstractStellarArgument<T, *>, R>(name: String, val argumentType: ArgumentType<R>? = null) : AbstractStellarCommand<T>(name) {

    /**
     * Represents the parent command, whether it's a command or an argument. This is automatically set internally.
     */
    open lateinit var parent: AbstractStellarCommand<*>
    override val globalFailureExecutions: MutableSet<StellarExecution<*>>
        get() = parent.globalFailureExecutions
    @ApiStatus.Internal open val suggestions: MutableSet<StellarSuggestion<*>> = mutableSetOf()
    @ApiStatus.Internal open var suggestionOffset: Int = 0

    /**
     * Adds a suggestion offset on top of the current offset.
     * The suggestion offset represents how many additional letters it will take for suggestions to appear.
     */
    fun addSuggestionOffset(offset: Int): T = apply { suggestionOffset += offset } as T
    /**
     * Sets the current suggestion offset amount to this offset.
     * The suggestion offset represents how many additional letters it will take for suggestions to appear.
     */
    fun setSuggestionOffset(offset: Int): T = apply { suggestionOffset = offset } as T

    /**
     * Adds multiple [Suggestion] on top of the current suggestions.
     */
    fun addSuggestions(vararg suggestions: Suggestion): T = apply {
        this.suggestions.add(StellarSuggestion(CommandSender::class) { CompletableFuture.completedFuture(suggestions.toList()) })
    } as T

    /**
     * Adds a [Suggestion] on top of the current suggestions.
     */
    fun addSuggestion(suggestion: Suggestion): T = addSuggestions(suggestion)
    /**
     * Adds a [Suggestion] with the given title and tooltip on top of the current suggestions.
     */
    fun addSuggestion(title: String, tooltip: String? = null): T = addSuggestions(Suggestion.create(title, tooltip))

    /**
     * Adds a function  that returns a list of suggestions in a [CompletableFuture] on top of the current suggestions.
     */
    inline fun <reified C : CommandSender> addFutureSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> CompletableFuture<Iterable<Suggestion>>): T = apply {
        suggestions.add(StellarSuggestion(C::class, suggestion))
    } as T

    /**
     * Adds an async function that returns a list of [Suggestion] on top of the current suggestions.
     */
    inline fun <reified C : CommandSender> addAsyncSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> List<Suggestion>): T = apply {
        suggestions.add(StellarSuggestion(C::class) { CompletableFuture.supplyAsync { suggestion(this, it) } })
    } as T

    /**
     * Adds a function that returns a list of [Suggestion] on top of the current suggestions.
     */
    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> List<Suggestion>): T = apply {
        suggestions.add(StellarSuggestion(C::class) { CompletableFuture.completedFuture(suggestion(this, it)) })
    } as T

    override fun hasGlobalHiddenDefaultFailureMessages(): Boolean = parent.hasGlobalHiddenDefaultFailureMessages()

    override fun setInformation(name: String, text: String): T = apply { parent.setInformation(name, text) } as T
    override fun setDescription(text: String): T = apply { parent.setDescription(text) } as T
    override fun setUsageText(text: String): T = apply { parent.setUsageText(text) } as T
    override fun clearInformation(): T = apply { parent.clearInformation() } as T

    override fun register(plugin: JavaPlugin): T = apply { parent.register(plugin) } as T

}