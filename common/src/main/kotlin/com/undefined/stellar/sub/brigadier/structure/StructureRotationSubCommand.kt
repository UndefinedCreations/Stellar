package com.undefined.stellar.sub.brigadier.structure

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.block.structure.StructureRotation
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class StructureRotationSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<StructureRotationSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addStructureRotationExecution(noinline execution: T.(StructureRotation) -> Unit): StructureRotationSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunStructureRotation(noinline execution: T.(StructureRotation) -> Boolean): StructureRotationSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
