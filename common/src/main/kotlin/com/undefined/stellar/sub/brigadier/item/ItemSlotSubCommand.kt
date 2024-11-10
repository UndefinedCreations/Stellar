package com.undefined.stellar.sub.brigadier.item

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

@Suppress("UNCHECKED_CAST")
class ItemSlotSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ItemSlotSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addItemSlotExecution(noinline execution: T.(Int) -> Unit): ItemSlotSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunItemSlot(noinline execution: T.(Int) -> Boolean): ItemSlotSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}