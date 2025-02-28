package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.generator.structure.StructureType

class StructureTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<StructureType>, StructureType>(parent, name, Registry.STRUCTURE_TYPE)