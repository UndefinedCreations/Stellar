package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class StellarSuggestion<C : CommandSender>(val clazz: KClass<C>, val suggestion: CommandContext<C>.() -> List<Suggestion>) {
    fun get(context: CommandContext<C>): List<Suggestion> {
        if (clazz.safeCast(context.sender) == null) return listOf()
        return suggestion(context)
    }
}