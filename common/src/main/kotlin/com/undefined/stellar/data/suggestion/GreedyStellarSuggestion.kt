package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.GreedyCommandContext
import org.bukkit.command.CommandSender

data class GreedyStellarSuggestion<C : CommandSender>(val suggestion: GreedyCommandContext<C>.() -> List<Suggestion>) {
    fun get(context: GreedyCommandContext<C>): List<Suggestion> = suggestion(context)
}