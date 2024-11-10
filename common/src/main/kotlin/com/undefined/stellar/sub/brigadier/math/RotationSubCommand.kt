package com.undefined.stellar.sub.brigadier.math

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.Operation
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.Location
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class RotationSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<RotationSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addRotationExecution(noinline execution: T.(Location) -> Unit): RotationSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunRotation(noinline execution: T.(Location) -> Boolean): RotationSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}