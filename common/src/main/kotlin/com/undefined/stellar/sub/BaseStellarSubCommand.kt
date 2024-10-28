package com.undefined.stellar.sub

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.StellarSuggestion
import org.bukkit.command.CommandSender

abstract class BaseStellarSubCommand<T>(val parent: BaseStellarCommand<*>, name: String) : BaseStellarCommand<BaseStellarSubCommand<*>>(name) {
    val suggestions: MutableList<StellarSuggestion<*>> = mutableListOf()

    fun addSuggestion(list: List<String>): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { list })
        return this as T
    }

    fun addSuggestion(vararg list: String): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { list.toList() })
        return this as T
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: C.(input: String) -> List<String>): T {
        suggestions.add(StellarSuggestion(C::class, suggestion))
        return this as T
    }

    override fun register() = parent.register()
}