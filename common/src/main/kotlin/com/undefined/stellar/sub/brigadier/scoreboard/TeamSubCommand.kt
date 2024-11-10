package com.undefined.stellar.sub.brigadier.scoreboard

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Team

@Suppress("UNCHECKED_CAST")
class TeamSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<TeamSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addTeamExecution(noinline execution: T.(Team) -> Unit): TeamSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunTeam(noinline execution: T.(Team) -> Boolean): TeamSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}