package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.block.banner.PatternType

class PatternTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<PatternTypeArgument, PatternType>(parent, name)