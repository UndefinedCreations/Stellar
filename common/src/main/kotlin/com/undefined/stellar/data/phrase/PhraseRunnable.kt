package com.undefined.stellar.data.phrase

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class PhraseRunnable<C : CommandSender>(val clazz: KClass<C>, val execution: PhraseCommandContext<C>.() -> Boolean) {
    operator fun invoke(context: PhraseCommandContext<CommandSender>): Boolean {
        if (clazz.safeCast(context.sender) == null) return true
        return execution(context as PhraseCommandContext<C>)
    }
}