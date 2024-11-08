package com.undefined.stellar.sub.brigadier.world

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.block.data.BlockData
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class BlockDataSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<BlockDataSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addBlockDataExecution(noinline execution: T.(BlockData) -> Unit): BlockDataSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunBlockData(noinline execution: T.(BlockData) -> Boolean): BlockDataSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
