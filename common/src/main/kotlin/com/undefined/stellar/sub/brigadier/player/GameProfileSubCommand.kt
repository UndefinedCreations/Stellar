package com.undefined.stellar.sub.brigadier.player

import com.mojang.authlib.GameProfile
import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class GameProfileSubCommand(parent: BaseStellarCommand<*>, name: String) : NativeTypeSubCommand<GameProfileSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addGameProfileExecution(noinline execution: T.(Collection<GameProfile>) -> Unit): GameProfileSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
