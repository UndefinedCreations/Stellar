package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.entity.EntityType

class EntityTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<EntityTypeArgument, EntityType>(parent, name)