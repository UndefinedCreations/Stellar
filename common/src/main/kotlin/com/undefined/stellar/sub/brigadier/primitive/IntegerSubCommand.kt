package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class IntegerSubCommand(parent: BaseStellarCommand<*>, name: String, val min: Int, val max: Int) : NativeTypeSubCommand<IntegerSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addIntegerExecution(noinline execution: T.(Int) -> Unit): IntegerSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
