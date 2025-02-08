package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.potion.PotionType

class PotionArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<PotionArgument, PotionType>(parent, name)