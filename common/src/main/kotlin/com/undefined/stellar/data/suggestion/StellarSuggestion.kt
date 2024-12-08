package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender

data class StellarSuggestion<C : CommandSender>(val suggestion: CommandContext<C>.() -> List<Suggestion>) {
    fun get(context: CommandContext<C>): List<Suggestion> = suggestion(context)
}