package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender

data class ReturnableStellarRunnable<C : CommandSender, V : Any?>(val runnable: CommandContext<C>.(V) -> Boolean) {
    operator fun invoke(context: CommandContext<CommandSender>, value: V): Boolean {
        return runnable(context as? CommandContext<C> ?: return true, value)
    }
}