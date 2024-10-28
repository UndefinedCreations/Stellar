package com.undefined.stellar.data.execution

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class StellarRunnable<T : CommandSender>(val kClass: KClass<T>, val execution: T.() -> Boolean) {
    fun run(sender: CommandSender): Boolean {
        return execution.invoke(kClass.safeCast(sender) ?: return false)
    }
}