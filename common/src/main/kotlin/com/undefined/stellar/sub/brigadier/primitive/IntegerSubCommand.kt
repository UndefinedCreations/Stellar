package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class IntegerSubCommand(parent: BaseStellarCommand, name: String, val min: Int, val max: Int) : NativeTypeSubCommand(parent, name) {
    inline fun <reified T : CommandSender> addIntegerExecution(noinline execution: T.(Int) -> Unit): BaseStellarCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
