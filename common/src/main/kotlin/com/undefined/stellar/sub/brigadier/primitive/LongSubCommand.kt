package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class LongSubCommand(parent: BaseStellarCommand<*>, name: String, val min: Long, val max: Long) : NativeTypeSubCommand<LongSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addLongExecution(noinline execution: T.(Long) -> Unit): LongSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunLong(noinline execution: T.(Long) -> Boolean): LongSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
