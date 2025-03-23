package com.undefined.stellar.data.phrase

import com.undefined.stellar.argument.phrase.WordArgument
import org.bukkit.command.CommandSender

/**
 * Represents a functional interface used for command runnables for [WordArgument].
 */
fun interface PhraseRunnable<C : CommandSender> {
	operator fun invoke(context: PhraseCommandContext<C>): Boolean
}