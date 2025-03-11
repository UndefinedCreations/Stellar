package com.undefined.stellar

import com.mojang.brigadier.arguments.ArgumentType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.execution.ExecutableExecution
import com.undefined.stellar.data.suggestion.ExecutableSuggestion
import com.undefined.stellar.data.suggestion.SimpleStellarSuggestion
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture

@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarArgument<T : AbstractStellarArgument<T, *>, R>(name: String, val argumentType: ArgumentType<R>? = null) : AbstractStellarCommand<T>(name) {

    open lateinit var parent: AbstractStellarCommand<*>
    override val globalFailureExecutions: MutableSet<ExecutableExecution<*>>
        get() = parent.globalFailureExecutions
    open val suggestions: MutableSet<ExecutableSuggestion<*>> = mutableSetOf()
    open var suggestionOffset: Int = 0

    fun addSuggestionOffset(offset: Int): T = apply { suggestionOffset += offset } as T
    fun setSuggestionOffset(offset: Int): T = apply { suggestionOffset = offset } as T

    fun addSuggestions(vararg suggestions: Suggestion): T = apply {
        this.suggestions.add(ExecutableSuggestion(CommandSender::class) { _, _ -> CompletableFuture.completedFuture(suggestions.toList()) })
    } as T

    fun addSuggestion(suggestion: Suggestion): T = addSuggestions(suggestion)
    fun addSuggestion(title: String, tooltip: String? = null): T = addSuggestions(Suggestion.create(title, tooltip))

    inline fun <reified C : CommandSender> addFutureSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> CompletableFuture<Iterable<Suggestion>>): T = apply {
        suggestions.add(ExecutableSuggestion(C::class, suggestion))
    } as T

    inline fun <reified C : CommandSender> addAsyncSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> List<Suggestion>): T = apply {
        suggestions.add(ExecutableSuggestion(C::class) { context, input -> CompletableFuture.supplyAsync { suggestion(context, input) } })
    } as T

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: CommandContext<C>.(input: String) -> List<Suggestion>): T = apply {
        suggestions.add(ExecutableSuggestion(C::class) { context, input -> CompletableFuture.completedFuture(suggestion(context, input)) })
    } as T

    fun addFutureSuggestion(suggestion: StellarSuggestion<CommandSender>): T = apply {
        suggestions.add(ExecutableSuggestion(CommandSender::class, suggestion))
    } as T

    fun addAsyncSuggestion(suggestion: SimpleStellarSuggestion<CommandSender>): T = apply {
        suggestions.add(ExecutableSuggestion(CommandSender::class) { context, input -> CompletableFuture.supplyAsync { suggestion(context, input) } })
    } as T

    fun addSuggestion(suggestion: SimpleStellarSuggestion<CommandSender>): T = apply {
        suggestions.add(ExecutableSuggestion(CommandSender::class) { context, input -> CompletableFuture.completedFuture(suggestion(context, input)) })
    } as T

    override fun hasGlobalHiddenDefaultFailureMessages(): Boolean = parent.hasGlobalHiddenDefaultFailureMessages()

    override fun setInformation(name: String, text: String): T = apply { parent.setInformation(name, text) } as T
    override fun setDescription(text: String): T = apply { parent.setDescription(text) } as T
    override fun setUsageText(text: String): T = apply { parent.setUsageText(text) } as T
    override fun clearInformation(): T = apply { parent.clearInformation() } as T

    override fun register(plugin: JavaPlugin): T = apply { parent.register(plugin) } as T

}