package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.entity.Wolf

class WolfVariantArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<WolfVariantArgument, Wolf.Variant>(parent, name)