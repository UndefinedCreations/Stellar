package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.entity.Villager

class VillagerProfessionArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Villager.Profession>, Villager.Profession>(parent, name, Registry.VILLAGER_PROFESSION)