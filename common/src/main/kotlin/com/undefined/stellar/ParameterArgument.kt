package com.undefined.stellar

import com.mojang.brigadier.arguments.ArgumentType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.*
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

/**
 * Being an extension of [AbstractStellarArgument], it represents an argument.
 *
 * @param name The name of the argument.
 * @param argumentType This is used internally and should not be used by the end user. Is used when the Brigadier library contains the argument type wanted.
 */
@Suppress("UNCHECKED_CAST")
abstract class ParameterArgument<T : ParameterArgument<T, *>, R>(name: String, val argumentType: ArgumentType<R>? = null) : AbstractStellarArgument<T>(name) {

    @ApiStatus.Internal
    open val suggestions: MutableSet<ExecutableSuggestion<*>> = mutableSetOf()
    @ApiStatus.Internal
    open var suggestionOffset: Int = 0

    /**
     * Adds a suggestion offset on top of the current offset.
     * The suggestion offset represents how many additional letters it will take for suggestions to appear.
     *
     * @return The modified [ParameterArgument].
     */
    fun addSuggestionOffset(offset: Int): T = apply { suggestionOffset += offset } as T

    /**
     * Sets the current suggestion offset amount to this offset.
     * The suggestion offset represents how many additional letters it will take for suggestions to appear.
     *
     * @return The modified [ParameterArgument].
     */
    fun setSuggestionOffset(offset: Int): T = apply { suggestionOffset = offset } as T

    /**
     * Adds a list of [Suggestion]s on top of the current suggestions.
     *
     * @return The modified [ParameterArgument].
     */
    fun addSuggestions(suggestions: List<Suggestion>): T = apply {
        this.suggestions.add(ExecutableSuggestion(CommandSender::class) { _, input ->
            CompletableFuture.completedFuture(suggestions.filter { it.text.startsWith(input) })
        })
    } as T

    /**
     * Adds a `vararg` of [Suggestion]s with the titles defined in [suggestions] on top of the current suggestions.
     *
     * @return The modified [ParameterArgument].
     */
    fun addSuggestions(vararg suggestions: String): T = apply {
        this.suggestions.add(ExecutableSuggestion(CommandSender::class) { _, input ->
            CompletableFuture.completedFuture(
                suggestions.filter { it.startsWith(input) }.map { it.toSuggestion() }
            )
        })
    } as T

    /**
     * Adds a [Suggestion] on top of the current suggestions.
     *
     * @return The modified [ParameterArgument].
     */
    fun addSuggestion(suggestion: Suggestion): T = addSuggestions(listOf(suggestion))

    /**
     * Adds a [Suggestion] with the given title and tooltip on top of the current suggestions.
     *
     * @return The modified [ParameterArgument].
     */
    @JvmOverloads
    fun addSuggestion(title: String, tooltip: String? = null): T = addSuggestions(listOf(Suggestion.create(title, tooltip)))

    /**
     * Adds a function  that returns a list of suggestions in a [CompletableFuture] on top of the current suggestions. Only works in Kotlin.
     *
     * @return The modified [ParameterArgument].
     */
    inline fun <reified C : CommandSender> addFutureSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> CompletableFuture<Iterable<Suggestion>>): T = apply {
        suggestions.add(ExecutableSuggestion(C::class, suggestion))
    } as T

    /**
     * Adds an async function that returns a list of [Suggestion] on top of the current suggestions. Only works in Kotlin.
     *
     * @return The modified [ParameterArgument].
     */
    inline fun <reified C : CommandSender> addAsyncSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> List<Suggestion>): T = apply {
        suggestions.add(ExecutableSuggestion(C::class) { context, input -> CompletableFuture.supplyAsync { suggestion(context, input) } })
    } as T

    /**
     * Adds a function that returns a list of [Suggestion] on top of the current suggestions. Only works in Kotlin.
     *
     * @return The modified [ParameterArgument].
     */
    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> List<Suggestion>): T =
        apply {
            suggestions.add(ExecutableSuggestion(C::class) { context, input ->
                CompletableFuture.completedFuture(
                    suggestion(context, input)
                )
            })
        } as T

    /**
     * Adds a function that returns a list of [Suggestion] on top of the current suggestions.
     *
     * @param sender The [Class] the sender will be cast into, which must be or extend [CommandSender].
     * If the cast is unsuccessful, then the function will not be run. **If you wish to just use a `CommandSender`, you can omit this parameter.**
     * @return The modified [ParameterArgument].
     */
    fun <C : CommandSender> addFutureSuggestion(sender: Class<C>, suggestion: StellarSuggestion<C>): T = apply {
        suggestions.add(ExecutableSuggestion(sender.kotlin, suggestion))
    } as T

    /**
     * Adds a function that returns a list of [Suggestion] on top of the current suggestions.
     *
     * @param sender The [Class] the sender will be cast into, which must be or extend [CommandSender].
     * If the cast is unsuccessful, then the function will not be run. **If you wish to just use a `CommandSender`, you can omit this parameter.**
     * @return The modified [ParameterArgument].
     */
    fun <C : CommandSender> addAsyncSuggestion(sender: Class<C>, suggestion: SimpleStellarSuggestion<C>): T = apply {
        suggestions.add(ExecutableSuggestion(sender.kotlin) { context, input -> CompletableFuture.supplyAsync { suggestion(context, input) } })
    } as T

    /**
     * Adds a function that returns a list of [Suggestion] on top of the current suggestions.
     *
     * @param sender The [Class] the sender will be cast into, which must be or extend [CommandSender].
     * If the cast is unsuccessful, then the function will not be run. **If you wish to just use a `CommandSender`, you can omit this parameter.**
     * @return The modified [ParameterArgument].
     */
    fun <C : CommandSender> addSuggestion(sender: Class<C>, suggestion: SimpleStellarSuggestion<C>): T = apply {
        suggestions.add(ExecutableSuggestion(sender.kotlin) { context, input -> CompletableFuture.completedFuture(suggestion(context, input)) })
    } as T

}