package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.entity.EntityType

class EntityTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<EntityType>, EntityType>(parent, name, Registry.ENTITY_TYPE)