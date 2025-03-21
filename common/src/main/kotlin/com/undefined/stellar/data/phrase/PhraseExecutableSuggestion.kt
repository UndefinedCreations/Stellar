package com.undefined.stellar.data.phrase

import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
@ApiStatus.Internal
data class PhraseExecutableSuggestion<C : CommandSender>(private val clazz: KClass<C>, private val suggestion: PhraseCommandContext<C>.() -> Iterable<Suggestion>) {
    fun get(context: PhraseCommandContext<CommandSender>): Iterable<Suggestion> {
        if (clazz.safeCast(context.sender) == null) return listOf()
        return suggestion(context as PhraseCommandContext<C>)
    }
}