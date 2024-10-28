package com.undefined.stellar.sub.brigadier.world

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.Location
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class Location2DSubCommand(parent: BaseStellarCommand<*>, name: String) : NativeTypeSubCommand<Location2DSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> add2DLocationExecution(noinline execution: T.(Location) -> Unit): Location2DSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRun2DLocation(noinline execution: T.(Location) -> Boolean): Location2DSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
