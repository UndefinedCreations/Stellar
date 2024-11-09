package com.undefined.stellar.sub.brigadier.text

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import net.kyori.adventure.text.format.Style
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.Objective

@Suppress("UNCHECKED_CAST")
class ObjectiveCriteriaSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ObjectiveCriteriaSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addObjectiveExecution(noinline execution: T.(String) -> Unit): ObjectiveCriteriaSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunObjective(noinline execution: T.(String) -> Boolean): ObjectiveCriteriaSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}