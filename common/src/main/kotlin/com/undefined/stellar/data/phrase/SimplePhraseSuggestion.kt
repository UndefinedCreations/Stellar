package com.undefined.stellar.data.phrase

import com.undefined.stellar.argument.phrase.WordArgument
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender

/**
 * Represents a functional interface used when creating a [WordArgument].
 */
fun interface SimplePhraseSuggestion<C : CommandSender> {
	operator fun invoke(context: PhraseCommandContext<C>): Iterable<Suggestion>
}