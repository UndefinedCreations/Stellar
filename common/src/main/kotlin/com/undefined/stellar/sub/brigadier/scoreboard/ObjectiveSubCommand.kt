package com.undefined.stellar.sub.brigadier.scoreboard

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.Objective

@Suppress("UNCHECKED_CAST")
class ObjectiveSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ObjectiveSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addObjectiveExecution(noinline execution: T.(Objective) -> Unit): ObjectiveSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunObjective(noinline execution: T.(Objective) -> Boolean): ObjectiveSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}