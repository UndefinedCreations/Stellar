package com.undefined.stellar.sub.brigadier.world

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.Location
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class LocationSubCommand(parent: BaseStellarCommand<*>, name: String) : NativeTypeSubCommand<LocationSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addLocationExecution(noinline execution: T.(Location) -> Unit): LocationSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunLocation(noinline execution: T.(Location) -> Boolean): LocationSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
