package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class StellarSuggestion<C : CommandSender>(private val clazz: KClass<C>, private val suggestion: CommandContext<C>.(input: String) -> CompletableFuture<Iterable<Suggestion>>) {
    fun get(context: CommandContext<CommandSender>, input: String): CompletableFuture<Iterable<Suggestion>> =
        clazz.safeCast(context.sender)?.let { suggestion(context as CommandContext<C>, input) } ?: CompletableFuture.completedFuture(listOf())
}