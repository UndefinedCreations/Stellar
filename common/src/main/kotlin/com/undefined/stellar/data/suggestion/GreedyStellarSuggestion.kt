package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.GreedyCommandContext
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class GreedyStellarSuggestion<C : CommandSender>(val clazz: KClass<C>, val suggestion: GreedyCommandContext<C>.() -> List<Suggestion>) {
    fun get(context: GreedyCommandContext<C>): List<Suggestion> {
        if (clazz.safeCast(context.sender) == null) return listOf()
        return suggestion(context)
    }
}