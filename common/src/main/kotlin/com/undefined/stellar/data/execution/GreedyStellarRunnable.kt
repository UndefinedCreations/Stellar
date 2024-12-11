package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.GreedyCommandContext
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
data class GreedyStellarRunnable<C : CommandSender>(val execution: GreedyCommandContext<C>.() -> Boolean) {
    operator fun invoke(context: GreedyCommandContext<CommandSender>): Boolean {
        return execution(context as? GreedyCommandContext<C> ?: return true)
    }
}