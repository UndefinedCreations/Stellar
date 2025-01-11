package com.undefined.stellar.data.suggestion

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
data class StellarSuggestion<C : CommandSender>(private val clazz: KClass<C>, private val suggestion: CommandContext<C>.() -> CompletableFuture<List<Suggestion>>) {
    fun get(context: CommandContext<CommandSender>): CompletableFuture<List<Suggestion>> {
        if (clazz.safeCast(context.sender) == null) return CompletableFuture.completedFuture(listOf())
        return suggestion(context as CommandContext<C>)
    }
}