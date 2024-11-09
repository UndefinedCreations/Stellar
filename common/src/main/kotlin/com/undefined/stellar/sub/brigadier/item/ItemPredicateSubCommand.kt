package com.undefined.stellar.sub.brigadier.item

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import java.util.function.Predicate

@Suppress("UNCHECKED_CAST")
class ItemPredicateSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ItemPredicateSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addItemPredicateExecution(noinline execution: T.(Predicate<ItemStack>) -> Unit): ItemPredicateSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunItemPredicate(noinline execution: T.(Predicate<ItemStack>) -> Boolean): ItemPredicateSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}