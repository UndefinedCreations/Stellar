package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender

data class StellarRunnable<C : CommandSender>(val execution: CommandContext<C>.() -> Boolean) {
    operator fun invoke(context: CommandContext<CommandSender>): Boolean {
        return execution(context as? CommandContext<C> ?: return true)
    }
}