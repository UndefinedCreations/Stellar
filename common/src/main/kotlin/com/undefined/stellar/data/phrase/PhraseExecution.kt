package com.undefined.stellar.data.phrase

import com.undefined.stellar.argument.phrase.WordArgument
import org.bukkit.command.CommandSender

/**
 * Represents a functional interface used for command executions for [WordArgument].
 */
fun interface PhraseExecution<C : CommandSender> {
	operator fun invoke(context: PhraseCommandContext<C>)
}