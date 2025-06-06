package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
@ApiStatus.Internal
data class ExecutableRunnable<C : CommandSender>(val alwaysApplicable: Boolean, val clazz: KClass<C>, val runnable: StellarRunnable<C>, val async: Boolean) {
    operator fun invoke(context: CommandContext<CommandSender>): Boolean {
        if (clazz.safeCast(context.sender) == null) return false
        return runnable(context as? CommandContext<C> ?: return false)
    }
}