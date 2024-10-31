package com.undefined.stellar.sub.brigadier.world

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.Material
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class BlockSubCommand(parent: BaseStellarCommand<*>, name: String) : NativeTypeSubCommand<LocationSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addBlockExecution(noinline execution: T.(Material) -> Unit): BlockSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunBlock(noinline execution: T.(Material) -> Boolean): BlockSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
