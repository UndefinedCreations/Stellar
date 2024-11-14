@file:Suppress("UNCHECKED_CAST")

package com.undefined.stellar.sub.brigadier.scoreboard

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender

class ScoreHoldersSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ScoreHoldersSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addScoreHoldersExecution(noinline execution: T.(List<String>) -> Unit): ScoreHoldersSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunScoreHolder(noinline execution: T.(List<String>) -> Boolean): ScoreHoldersSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}

class ScoreHolderSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ScoreHolderSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addScoreHolderExecution(noinline execution: T.(String) -> Unit): ScoreHolderSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunScoreHolder(noinline execution: T.(String) -> Boolean): ScoreHolderSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
