package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

fun interface SimpleStellarSuggestion<C : CommandSender> {

    operator fun invoke(context: CommandContext<C>, input: String): List<Suggestion>
//    override fun invoke(context: CommandContext<C>, input: String): CompletableFuture<Iterable<Suggestion>>
}