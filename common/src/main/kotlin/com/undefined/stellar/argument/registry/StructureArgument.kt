package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.structure.Structure

class StructureArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<StructureArgument, Structure>(parent, name)