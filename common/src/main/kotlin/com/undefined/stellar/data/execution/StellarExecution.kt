package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender

data class StellarExecution<T : CommandSender>(val execution: CommandContext<T>.() -> Unit) {
    operator fun invoke(context: CommandContext<CommandSender>) {
        execution(context as? CommandContext<T> ?: return)
    }
}