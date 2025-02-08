package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.entity.Villager

class VillagerTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<VillagerTypeArgument, Villager.Type>(parent, name)