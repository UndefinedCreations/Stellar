package com.undefined.stellar

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class StellarRequirement<T : CommandSender>(val kClass: KClass<T>, val execute: T.() -> Boolean) {

    fun run(sender: CommandSender): Boolean {
        return execute.invoke(kClass.safeCast(sender) ?: return false)
    }

}