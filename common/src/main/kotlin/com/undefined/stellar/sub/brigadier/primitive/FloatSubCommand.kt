package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class FloatSubCommand(parent: BaseStellarCommand<*>, name: String, val min: Float, val max: Float) : NativeTypeSubCommand<FloatSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addFloatExecution(noinline execution: T.(Float) -> Unit): FloatSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
