package com.undefined.stellar.data.execution

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class CustomStellarExecution<T : CommandSender, V>(val kClass: KClass<T>, val execution: T.(V) -> Unit) {
    fun run(sender: CommandSender, value: V) {
        execution.invoke(kClass.safeCast(sender) ?: return, value)
    }
}