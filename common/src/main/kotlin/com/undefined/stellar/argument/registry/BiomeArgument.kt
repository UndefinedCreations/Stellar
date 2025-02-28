package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.block.Biome

class BiomeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Biome>, Biome>(parent, name, Registry.BIOME)