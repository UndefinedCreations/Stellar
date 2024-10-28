package com.undefined.stellar.sub

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.StellarSuggestion
import org.bukkit.command.CommandSender

abstract class BaseStellarSubCommand<T>(val parent: BaseStellarCommand<*>, name: String) : BaseStellarCommand<BaseStellarSubCommand<*>>(name) {
    val suggestions: MutableList<StellarSuggestion<*>> = mutableListOf()

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: C.(input: String) -> List<String>): T {
        suggestions.add(StellarSuggestion(C::class, suggestion))
        return this as T
    }

    override fun register() = parent.register()
}