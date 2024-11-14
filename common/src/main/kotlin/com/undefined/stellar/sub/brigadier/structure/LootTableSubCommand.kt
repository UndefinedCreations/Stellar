package com.undefined.stellar.sub.brigadier.structure

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.loot.LootTable

@Suppress("UNCHECKED_CAST")
class LootTableSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<LootTableSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addLootTableExecution(noinline execution: T.(LootTable) -> Unit): LootTableSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunLootTable(noinline execution: T.(LootTable) -> Boolean): LootTableSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
