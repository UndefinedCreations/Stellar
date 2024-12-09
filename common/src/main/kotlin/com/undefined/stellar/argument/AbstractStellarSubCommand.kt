package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarArgument<T>(val parent: AbstractStellarCommand<*>, name: String) : AbstractStellarCommand<T>(name) {

    @ApiStatus.Internal open val suggestions: MutableList<StellarSuggestion<*>> = mutableListOf()

    fun addSuggestion(text: String, tooltip: String): T {
        addSuggestion(Suggestion(text, tooltip))
        return this as T
    }

    fun addSuggestion(suggestion: Suggestion): T {
        suggestions.add(StellarSuggestion<CommandSender> { listOf(suggestion) })
        return this as T
    }

    fun addSuggestions(list: List<Suggestion>): T {
        suggestions.add(StellarSuggestion<CommandSender> { list })
        return this as T
    }

    fun addSuggestions(vararg list: Suggestion): T {
        suggestions.add(StellarSuggestion<CommandSender> { list.toList() })
        return this as T
    }

    fun addSuggestionsWithoutTooltip(list: List<String>): T {
        suggestions.add(StellarSuggestion<CommandSender> { list.map { Suggestion(it, "") } })
        return this as T
    }

    fun addSuggestions(vararg list: String): T {
        suggestions.add(StellarSuggestion<CommandSender> { list.map { Suggestion(it, "") } })
        return this as T
    }

    fun setSuggestions(vararg suggestion: Suggestion): T {
        suggestions.clear()
        suggestions.add(StellarSuggestion<CommandSender> { suggestion.toList() })
        return this as T
    }

    fun setSuggestions(vararg suggestion: String): T {
        suggestions.clear()
        suggestions.add(StellarSuggestion<CommandSender> { suggestion.map { Suggestion(it, "") } })
        return this as T
    }

    fun setSuggestions(suggestion: List<Suggestion>): T {
        suggestions.clear()
        suggestions.add(StellarSuggestion<CommandSender> { suggestion.toList() })
        return this as T
    }

    fun setSuggestionsWithoutTooltip(suggestion: List<String>): T {
        suggestions.clear()
        suggestions.add(StellarSuggestion<CommandSender> { suggestion.map { Suggestion(it, "") } })
        return this as T
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: CommandContext<C>.() -> List<Suggestion>): T {
        suggestions.add(StellarSuggestion(suggestion))
        return this as T
    }

    override val base: AbstractStellarCommand<*>
        get() = parent.base
    override fun register(plugin: JavaPlugin) = parent.register(plugin)

}