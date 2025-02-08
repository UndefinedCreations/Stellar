package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.inventory.meta.trim.TrimPattern

class TrimPatternArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<TrimPatternArgument, TrimPattern>(parent, name)