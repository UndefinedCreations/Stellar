package com.undefined.stellar.sub.brigadier.item

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class ItemSlotsSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<ItemSlotsSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addItemSlotsExecution(noinline execution: T.(List<Int>) -> Unit): ItemSlotsSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunItemSlots(noinline execution: T.(List<Int>) -> Boolean): ItemSlotsSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}