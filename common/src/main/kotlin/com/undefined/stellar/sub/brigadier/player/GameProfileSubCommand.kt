package com.undefined.stellar.sub.brigadier.player

import com.mojang.authlib.GameProfile
import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class GameProfileSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<GameProfileSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addGameProfileExecution(noinline execution: T.(Collection<GameProfile>) -> Unit): GameProfileSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunGameProfile(noinline execution: T.(GameProfile) -> Boolean): GameProfileSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
