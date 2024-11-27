package com.undefined.stellar.sub.brigadier.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.Location
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class RotationSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<RotationSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addRotationExecution(noinline execution: T.(Location) -> Unit): RotationSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunRotation(noinline execution: T.(Location) -> Boolean): RotationSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}