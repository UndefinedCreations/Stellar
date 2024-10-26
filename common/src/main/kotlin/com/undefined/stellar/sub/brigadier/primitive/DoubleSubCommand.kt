package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class DoubleSubCommand(parent: BaseStellarCommand, name: String, val min: Double, val max: Double) : NativeTypeSubCommand(parent, name) {
    inline fun <reified T : CommandSender> addDoubleExecute(noinline execution: T.(Double) -> Unit): BaseStellarCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
