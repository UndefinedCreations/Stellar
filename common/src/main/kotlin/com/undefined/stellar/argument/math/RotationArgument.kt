package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.Location

class RotationArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<RotationArgument, Location>(parent, name)