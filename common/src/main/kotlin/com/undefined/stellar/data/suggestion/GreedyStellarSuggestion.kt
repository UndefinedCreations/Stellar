package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.argument.GreedyCommandContext
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class GreedyStellarSuggestion<C : CommandSender>(private val clazz: KClass<C>, private val suggestion: GreedyCommandContext<C>.() -> List<Suggestion>) {
    fun get(context: GreedyCommandContext<CommandSender>): List<Suggestion> {
        if (clazz.safeCast(context.sender) == null) return listOf()
        return suggestion(context as GreedyCommandContext<C>)
    }
}