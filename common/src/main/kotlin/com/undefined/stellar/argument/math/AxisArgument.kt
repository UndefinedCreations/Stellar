package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class AxisArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<AxisArgument, Float>(parent, name)