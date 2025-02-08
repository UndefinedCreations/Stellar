package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.damage.DamageType

class DamageTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<DamageTypeArgument, DamageType>(parent, name)