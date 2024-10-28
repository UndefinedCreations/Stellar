package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class DoubleSubCommand(parent: BaseStellarCommand<*>, name: String, val min: Double, val max: Double) : NativeTypeSubCommand<DoubleSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addDoubleExecution(noinline execution: T.(Double) -> Unit): DoubleSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
