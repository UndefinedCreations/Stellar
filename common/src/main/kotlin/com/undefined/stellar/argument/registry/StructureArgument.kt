package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.generator.structure.Structure

class StructureArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Structure>, Structure>(parent, name, Registry.STRUCTURE)