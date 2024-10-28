package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import com.undefined.stellar.sub.brigadier.player.GameProfileSubCommand
import org.bukkit.command.CommandSender

class BooleanSubCommand(parent: BaseStellarCommand<*>, name: String) : NativeTypeSubCommand<BooleanSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addBooleanExecution(noinline execution: T.(Boolean) -> Unit): BooleanSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
