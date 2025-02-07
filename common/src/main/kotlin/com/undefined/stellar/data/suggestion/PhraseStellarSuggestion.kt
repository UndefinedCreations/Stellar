package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.PhraseCommandContext
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class PhraseStellarSuggestion<C : CommandSender>(private val clazz: KClass<C>, private val suggestion: PhraseCommandContext<C>.() -> List<Suggestion>) {
    fun get(context: PhraseCommandContext<CommandSender>): List<Suggestion> {
        if (clazz.safeCast(context.sender) == null) return listOf()
        return suggestion(context as PhraseCommandContext<C>)
    }
}