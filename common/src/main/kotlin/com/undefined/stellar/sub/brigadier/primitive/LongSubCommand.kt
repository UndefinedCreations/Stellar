package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class LongSubCommand(parent: BaseStellarCommand, name: String, val min: Long, val max: Long) : NativeTypeSubCommand(parent, name) {
    inline fun <reified T : CommandSender> addLongExecution(noinline execution: T.(Long) -> Unit): BaseStellarCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
