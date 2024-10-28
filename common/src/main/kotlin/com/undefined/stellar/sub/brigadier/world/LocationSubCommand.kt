package com.undefined.stellar.sub.brigadier.world

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.Location
import org.bukkit.command.CommandSender

class LocationSubCommand(parent: BaseStellarCommand, name: String) : NativeTypeSubCommand(parent, name) {
    inline fun <reified T : CommandSender> addLocationExecution(noinline execution: T.(Location) -> Unit): BaseStellarCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
