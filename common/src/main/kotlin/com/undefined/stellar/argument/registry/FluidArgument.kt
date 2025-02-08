package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.Fluid

class FluidArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<FluidArgument, Fluid>(parent, name)