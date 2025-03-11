package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender

fun interface StellarRunnable<C : CommandSender> {
    operator fun invoke(context: CommandContext<C>): Boolean
}