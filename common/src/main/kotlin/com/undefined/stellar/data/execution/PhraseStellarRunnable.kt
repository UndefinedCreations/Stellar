package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.PhraseCommandContext
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class PhraseStellarRunnable<C : CommandSender>(val clazz: KClass<C>, val execution: PhraseCommandContext<C>.() -> Boolean) {
    operator fun invoke(context: PhraseCommandContext<CommandSender>): Boolean {
        if (clazz.safeCast(context.sender) == null) return true
        return execution(context as PhraseCommandContext<C>)
    }
}