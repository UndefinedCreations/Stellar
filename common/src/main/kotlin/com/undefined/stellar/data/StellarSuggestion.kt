package com.undefined.stellar.data

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class StellarSuggestion<T : CommandSender>(val kClass: KClass<T>, val suggestion: T.(input: String) -> List<String>) {
    fun get(sender: CommandSender, input: String): List<String> {
        return suggestion(kClass.safeCast(sender) ?: return listOf(), input)
    }
}