package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class StellarSuggestion<C : CommandSender>(private val clazz: KClass<C>, private val suggestion: CommandContext<C>.() -> List<Suggestion>) {
    fun get(context: CommandContext<CommandSender>): List<Suggestion> {
        if (clazz.safeCast(context.sender) == null) return listOf()
        return suggestion(context as CommandContext<C>)
    }
}