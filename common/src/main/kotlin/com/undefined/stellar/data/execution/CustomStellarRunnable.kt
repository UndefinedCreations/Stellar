package com.undefined.stellar.data.execution

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class CustomStellarRunnable<T : CommandSender, V>(val kClass: KClass<T>, val execution: T.(V) -> Boolean) {
    fun run(sender: CommandSender, value: V): Boolean {
        return execution.invoke(kClass.safeCast(sender) ?: return false, value)
    }
}