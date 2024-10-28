package com.undefined.stellar.data.requirement

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class StellarRequirement<T : CommandSender>(val kClass: KClass<T>, val execution: T.() -> Boolean) {

    fun run(sender: CommandSender): Boolean {
        return execution.invoke(kClass.safeCast(sender) ?: return false)
    }

}