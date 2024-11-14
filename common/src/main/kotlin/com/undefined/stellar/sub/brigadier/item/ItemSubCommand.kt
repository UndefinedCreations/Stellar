package com.undefined.stellar.sub.brigadier.item

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

@Suppress("UNCHECKED_CAST")
class ItemSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ItemSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addItemExecution(noinline execution: T.(ItemStack) -> Unit): ItemSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunItem(noinline execution: T.(ItemStack) -> Boolean): ItemSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}