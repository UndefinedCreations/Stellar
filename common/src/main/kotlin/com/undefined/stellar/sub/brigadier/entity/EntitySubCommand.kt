package com.undefined.stellar.sub.brigadier.entity

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity

class EntitySubCommand(parent: BaseStellarCommand, name: String, val type: EntityDisplayType) : NativeTypeSubCommand(parent, name) {
    inline fun <reified T : CommandSender> addEntitySubCommand(noinline execution: T.(Entity) -> Unit): BaseStellarCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}

enum class EntityDisplayType {
    ENTITY,
    ENTITIES,
    PLAYER,
    PLAYERS
}