package com.undefined.stellar.argument.entity

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class EntityArgument(parent: AbstractStellarCommand<*>, name: String, val type: com.undefined.stellar.argument.entity.EntityDisplayType) : AbstractStellarArgument<com.undefined.stellar.argument.entity.EntityArgument>(parent, name)

enum class EntityDisplayType {
    ENTITY,
    ENTITIES,
    PLAYER,
    PLAYERS
}