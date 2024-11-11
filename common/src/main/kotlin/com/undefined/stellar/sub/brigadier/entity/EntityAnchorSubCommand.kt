package com.undefined.stellar.sub.brigadier.entity

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.arguments.Anchor
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity

@Suppress("UNCHECKED_CAST")
class EntityAnchorSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<EntityAnchorSubCommand>(parent, name) {

    inline fun <reified T : CommandSender> addEntityAnchorSubCommand(noinline execution: T.(Anchor) -> Unit): EntityAnchorSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunEntityAnchor(noinline execution: T.(Anchor) -> Boolean): EntityAnchorSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }

}
