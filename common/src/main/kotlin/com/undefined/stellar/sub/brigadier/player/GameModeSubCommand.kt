package com.undefined.stellar.sub.brigadier.player

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.GameMode
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class GameModeSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<GameModeSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addGameModeExecution(noinline execution: T.(GameMode) -> Unit): GameModeSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunGameMode(noinline execution: T.(GameMode) -> Boolean): GameModeSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}
