package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.block.banner.PatternType

class PatternTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<PatternType>, PatternType>(parent, name, Registry.BANNER_PATTERN)