package com.undefined.stellar.sub.brigadier.structure

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.block.structure.Mirror
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class MirrorSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<MirrorSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addMirrorExecution(noinline execution: T.(Mirror) -> Unit): MirrorSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunMirror(noinline execution: T.(Mirror) -> Boolean): MirrorSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}
