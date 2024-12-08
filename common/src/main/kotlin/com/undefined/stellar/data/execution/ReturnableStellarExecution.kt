package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender

data class ReturnableStellarExecution<C : CommandSender, V : Any?>(val execution: CommandContext<C>.(V) -> Unit) {
    operator fun invoke(context: CommandContext<CommandSender>, value: V) {
        execution(context as? CommandContext<C> ?: return, value)
    }
}