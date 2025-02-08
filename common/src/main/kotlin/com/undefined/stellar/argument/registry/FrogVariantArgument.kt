package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.entity.Frog

class FrogVariantArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<FrogVariantArgument, Frog.Variant>(parent, name)