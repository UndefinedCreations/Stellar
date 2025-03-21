package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender

/**
 * Represents a functional interface used for command suggestions that solely return an [Iterable] of [Suggestion].
 */
fun interface SimpleStellarSuggestion<C : CommandSender> {
    operator fun invoke(context: CommandContext<C>, input: String): Iterable<Suggestion>
}