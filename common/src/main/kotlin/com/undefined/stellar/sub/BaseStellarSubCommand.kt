package com.undefined.stellar.sub

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

abstract class BaseStellarSubCommand<T>(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarSubCommand<T>(parent, name) {

    val suggestions: MutableList<StellarSuggestion<*>> = mutableListOf()
    val customExecutions: MutableList<CustomStellarExecution<*, Any?>> = mutableListOf()
    val customRunnables: MutableList<CustomStellarRunnable<*, Any?>> = mutableListOf()

    fun addSuggestion(text: String, tooltip: String): T {
        addSuggestion(Suggestion(text, tooltip))
        return this as T
    }

    fun addSuggestion(suggestion: Suggestion): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { listOf(suggestion) })
        return this as T
    }

    fun addSuggestions(list: List<Suggestion>): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { list })
        return this as T
    }

    fun addSuggestions(vararg list: Suggestion): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { list.toList() })
        return this as T
    }

    fun addSuggestionsWithoutTooltip(list: List<String>): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { list.map { Suggestion(it, "") } })
        return this as T
    }

    fun addSuggestions(vararg list: String): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { list.map { Suggestion(it, "") } })
        return this as T
    }

    fun setSuggestions(vararg suggestion: Suggestion): T {
        suggestions.clear()
        suggestions.add(StellarSuggestion(CommandSender::class) { suggestion.toList() })
        return this as T
    }

    fun setSuggestions(vararg suggestion: String): T {
        suggestions.clear()
        suggestions.add(StellarSuggestion(CommandSender::class) { suggestion.map { Suggestion(it, "") } })
        return this as T
    }

    fun setSuggestions(suggestion: List<Suggestion>): T {
        suggestions.clear()
        suggestions.add(StellarSuggestion(CommandSender::class) { suggestion.toList() })
        return this as T
    }

    fun setSuggestionsWithoutTooltip(suggestion: List<String>): T {
        suggestions.clear()
        suggestions.add(StellarSuggestion(CommandSender::class) { suggestion.map { Suggestion(it, "") } })
        return this as T
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: C.(input: String) -> List<Suggestion>): T {
        suggestions.add(StellarSuggestion(C::class, suggestion))
        return this as T
    }

    inline fun <reified C : CommandSender> addSubCommandExecution(noinline execution: C.(CommandContext) -> Unit): T {
        customExecutions.add(CustomStellarExecution(C::class, execution) as CustomStellarExecution<*, Any?>)
        return this as T
    }

    override fun getBase(): AbstractStellarCommand<*> = parent.getBase()
    override fun register(plugin: JavaPlugin) = parent.register(plugin)

}