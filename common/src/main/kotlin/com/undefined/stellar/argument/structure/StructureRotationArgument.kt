package com.undefined.stellar.argument.structure

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.block.structure.StructureRotation

class StructureRotationArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<StructureRotationArgument, StructureRotation>(parent, name)