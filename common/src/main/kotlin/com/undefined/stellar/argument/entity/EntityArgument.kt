package com.undefined.stellar.argument.entity

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.entity.Entity

class EntityArgument(name: String, val type: EntityDisplayType) : AbstractStellarArgument<EntityArgument, Entity>(name)

enum class EntityDisplayType {
    ENTITY,
    ENTITIES,
    PLAYER,
    PLAYERS
}