package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Fluid
import org.bukkit.Registry

class FluidArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Fluid>, Fluid>(parent, name, Registry.FLUID)