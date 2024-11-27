package com.undefined.stellar.sub.brigadier.world

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.HeightMap
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class HeightMapSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<HeightMapSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addHeightMapExecution(noinline execution: T.(HeightMap) -> Unit): HeightMapSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunHeightMap(noinline execution: T.(HeightMap) -> Boolean): HeightMapSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}