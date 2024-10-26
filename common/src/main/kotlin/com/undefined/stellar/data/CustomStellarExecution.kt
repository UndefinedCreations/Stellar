package com.undefined.stellar.data

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class CustomStellarExecution<T : CommandSender, V>(val kClass: KClass<T>, val execute: T.(V) -> Unit) {
    fun run(sender: CommandSender, value: V) {
        execute.invoke(kClass.safeCast(sender) ?: return, value)
    }
}