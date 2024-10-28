package com.undefined.stellar.data.execution

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class StellarExecution<T : CommandSender>(val kClass: KClass<T>, val execution: T.() -> Unit) {
    fun run(sender: CommandSender) {
        execution.invoke(kClass.safeCast(sender) ?: return)
    }
}