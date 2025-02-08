package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.generator.structure.StructureType

class StructureTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<StructureTypeArgument, StructureType>(parent, name)