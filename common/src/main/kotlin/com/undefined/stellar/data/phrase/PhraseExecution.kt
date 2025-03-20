package com.undefined.stellar.data.phrase

import org.bukkit.command.CommandSender

fun interface PhraseExecution<C : CommandSender> {
	operator fun invoke(context: PhraseCommandContext<C>)
}