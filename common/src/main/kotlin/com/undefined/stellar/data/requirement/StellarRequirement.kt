package com.undefined.stellar.data.requirement

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

data class StellarRequirement<C : CommandSender>(val clazz: KClass<C>, val requirement: C.() -> Boolean) {
    operator fun invoke(sender: CommandSender): Boolean {
        return requirement(clazz.safeCast(sender) ?: return true)
    }
}