package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.inventory.meta.trim.TrimMaterial

class TrimMaterialArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<TrimMaterialArgument, TrimMaterial>(parent, name)