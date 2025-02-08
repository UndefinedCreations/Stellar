package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.entity.Villager

class VillagerProfessionArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<VillagerProfessionArgument, Villager.Profession>(parent, name)