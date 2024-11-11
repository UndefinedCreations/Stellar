package com.undefined.stellar.sub.brigadier.math

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class RangeSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<RangeSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addRangeExecution(noinline execution: T.(IntRange) -> Unit): RangeSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunRange(noinline execution: T.(IntRange) -> Boolean): RangeSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}