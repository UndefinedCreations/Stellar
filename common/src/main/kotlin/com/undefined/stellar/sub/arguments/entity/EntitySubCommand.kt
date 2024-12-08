package com.undefined.stellar.sub.arguments.entity

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

class EntitySubCommand(parent: AbstractStellarCommand<*>, name: String, val type: EntityDisplayType) : AbstractStellarSubCommand<EntitySubCommand>(parent, name)

enum class EntityDisplayType {
    ENTITY,
    ENTITIES,
    PLAYER,
    PLAYERS
}