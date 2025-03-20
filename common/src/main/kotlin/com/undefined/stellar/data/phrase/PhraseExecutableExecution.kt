package com.undefined.stellar.data.phrase

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class PhraseExecutableExecution<C : CommandSender>(val clazz: KClass<C>, val execution: PhraseExecution<C>) {
    operator fun invoke(context: PhraseCommandContext<CommandSender>) {
        if (clazz.safeCast(context.sender) == null) return
        execution(context as PhraseCommandContext<C>)
    }
}