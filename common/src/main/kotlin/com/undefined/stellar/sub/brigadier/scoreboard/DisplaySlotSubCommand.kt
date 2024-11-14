package com.undefined.stellar.sub.brigadier.scoreboard

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.DisplaySlot

@Suppress("UNCHECKED_CAST")
class DisplaySlotSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<DisplaySlotSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addDisplaySlotExecution(noinline execution: T.(DisplaySlot) -> Unit): DisplaySlotSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunDisplaySlot(noinline execution: T.(DisplaySlot) -> Boolean): DisplaySlotSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}