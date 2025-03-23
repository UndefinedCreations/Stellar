package com.undefined.stellar.data.phrase

import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
@ApiStatus.Internal
data class PhraseExecutableRunnable<C : CommandSender>(val clazz: KClass<C>, val execution: PhraseRunnable<C>) {
    operator fun invoke(context: PhraseCommandContext<CommandSender>): Boolean {
        if (clazz.safeCast(context.sender) == null) return true
        return execution(context as PhraseCommandContext<C>)
    }
}