package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.GreedyCommandContext
import org.bukkit.command.CommandSender

data class GreedyStellarExecution<T : CommandSender>(val execution: GreedyCommandContext<T>.() -> Unit) {
    operator fun invoke(context: GreedyCommandContext<CommandSender>) {
        execution(context as? GreedyCommandContext<T> ?: return)
    }
}