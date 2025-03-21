package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

/**
 * Represents a functional interface used for command suggestions that return an [CompletableFuture] with an [Iterable] of [Suggestion].
 */
fun interface StellarSuggestion<C : CommandSender> {
    operator fun invoke(context: CommandContext<C>, input: String): CompletableFuture<Iterable<Suggestion>>
}