package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.block.Biome

class BiomeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<BiomeArgument, Biome>(parent, name)