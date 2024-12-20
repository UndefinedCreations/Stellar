package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.PhraseCommandContext
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class PhraseStellarExecution<C : CommandSender>(val clazz: KClass<C>, val execution: PhraseCommandContext<C>.() -> Unit) {
    operator fun invoke(context: PhraseCommandContext<CommandSender>) {
        if (clazz.safeCast(context.sender) == null) return
        execution(context as PhraseCommandContext<C>)
    }
}