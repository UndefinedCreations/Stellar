package com.undefined.stellar.data.requirement

import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@ApiStatus.Internal
data class ExecutableRequirement<C : CommandSender>(val clazz: KClass<C>, val requirement: StellarRequirement<C>) {
    operator fun invoke(sender: CommandSender): Boolean {
        return requirement(clazz.safeCast(sender) ?: return true)
    }
}