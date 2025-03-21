package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
@ApiStatus.Internal
data class ExecutableExecution<C : CommandSender>(val clazz: KClass<C>, val execution: StellarExecution<C>, val async: Boolean) {
    operator fun invoke(context: CommandContext<CommandSender>) {
        if (clazz.safeCast(context.sender) == null) return
        execution(context as? CommandContext<C> ?: return)
    }
}