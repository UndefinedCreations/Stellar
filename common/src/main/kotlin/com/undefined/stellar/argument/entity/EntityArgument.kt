package com.undefined.stellar.argument.entity

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.entity.Entity

class EntityArgument(parent: AbstractStellarCommand<*>, name: String, val type: EntityDisplayType) : AbstractStellarArgument<EntityArgument, Entity>(parent, name)

enum class EntityDisplayType {
    ENTITY,
    ENTITIES,
    PLAYER,
    PLAYERS
}