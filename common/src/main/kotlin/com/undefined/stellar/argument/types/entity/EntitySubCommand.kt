package com.undefined.stellar.argument.types.entity

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class EntityArgument(parent: AbstractStellarCommand<*>, name: String, val type: EntityDisplayType) : AbstractStellarArgument<EntityArgument>(parent, name)

enum class EntityDisplayType {
    ENTITY,
    ENTITIES,
    PLAYER,
    PLAYERS
}