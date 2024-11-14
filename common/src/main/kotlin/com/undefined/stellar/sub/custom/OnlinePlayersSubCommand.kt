package com.undefined.stellar.sub.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Suppress("UNCHECKED_CAST")
class OnlinePlayersSubCommand(parent: AbstractStellarCommand<*>, name: String, val players: () -> List<Player>) : ListSubCommand<Player>(parent, name, players, { it.name }, { Bukkit.getPlayer(it) }) {
    inline fun <reified C : CommandSender> addOnlinePlayersExecution(noinline execution: C.(Player) -> Unit): OnlinePlayersSubCommand {
        customExecutions.add(CustomStellarExecution(C::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified C : CommandSender> alwaysRunOnlinePlayers(noinline execution: C.(Player) -> Boolean): OnlinePlayersSubCommand {
        customRunnables.add(CustomStellarRunnable(C::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}