package com.undefined.stellar.sub

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.StellarSuggestion
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import org.bukkit.command.CommandSender

abstract class BaseStellarSubCommand<T>(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarSubCommand<T>(parent, name) {
    val suggestions: MutableList<StellarSuggestion<*>> = mutableListOf()
    val customExecutions: MutableList<CustomStellarExecution<*, Any?>> = mutableListOf()
    val customRunnables: MutableList<CustomStellarRunnable<*, Any?>> = mutableListOf()

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

}