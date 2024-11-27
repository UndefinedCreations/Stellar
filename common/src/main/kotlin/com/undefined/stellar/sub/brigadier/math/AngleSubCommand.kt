package com.undefined.stellar.sub.brigadier.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class AngleSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<AngleSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addAngleExecution(noinline execution: T.(Float) -> Unit): AngleSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunAngle(noinline execution: T.(Float) -> Boolean): AngleSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}