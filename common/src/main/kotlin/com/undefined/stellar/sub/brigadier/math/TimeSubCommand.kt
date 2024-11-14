package com.undefined.stellar.sub.brigadier.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import java.time.Duration

@Suppress("UNCHECKED_CAST")
class TimeSubCommand(parent: AbstractStellarCommand<*>, name: String, val minimum: Int) : BrigadierTypeSubCommand<TimeSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addTimeExecution(noinline execution: T.(Duration) -> Unit): TimeSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunTime(noinline execution: T.(Duration) -> Boolean): TimeSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}