package com.undefined.stellar.data.execution

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

/**
 * Represents a functional interface used for command executions.
 */
fun interface StellarRunnable<C : CommandSender> {
    operator fun invoke(context: CommandContext<C>): CompletableFuture<Boolean>
}