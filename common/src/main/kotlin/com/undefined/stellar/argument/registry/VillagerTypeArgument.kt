package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.entity.Villager

class VillagerTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Villager.Type>, Villager.Type>(parent, name, Registry.VILLAGER_TYPE)