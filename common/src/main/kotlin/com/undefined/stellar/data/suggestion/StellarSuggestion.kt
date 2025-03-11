package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

fun interface StellarSuggestion<C : CommandSender> {
    operator fun invoke(context: CommandContext<C>, input: String): CompletableFuture<Iterable<Suggestion>>
}