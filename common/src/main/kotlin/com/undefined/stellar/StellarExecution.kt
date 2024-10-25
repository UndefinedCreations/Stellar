package com.undefined.stellar

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class StellarExecution<T : CommandSender>(val kClass: KClass<T>, val execute: T.() -> Unit) {

    fun run(sender: CommandSender) {
        execute.invoke(kClass.safeCast(sender) ?: return)
    }

}