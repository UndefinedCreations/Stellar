package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.GreedyCommandContext
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class GreedyStellarRunnable<C : CommandSender>(val clazz: KClass<C>, val execution: GreedyCommandContext<C>.() -> Boolean) {
    operator fun invoke(context: GreedyCommandContext<CommandSender>): Boolean {
        if (clazz.safeCast(context.sender) == null) return true
        return execution(context as GreedyCommandContext<C>)
    }
}