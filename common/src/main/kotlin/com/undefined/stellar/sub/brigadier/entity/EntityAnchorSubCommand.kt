package com.undefined.stellar.sub.brigadier.entity

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.argument.Anchor
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class EntityAnchorSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<EntityAnchorSubCommand>(parent, name) {

    inline fun <reified T : CommandSender> addEntityAnchorSubCommand(noinline execution: T.(Anchor) -> Unit): EntityAnchorSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunEntityAnchor(noinline execution: T.(Anchor) -> Boolean): EntityAnchorSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }

}
