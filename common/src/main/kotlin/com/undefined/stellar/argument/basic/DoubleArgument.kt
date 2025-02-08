package com.undefined.stellar.argument.basic

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class DoubleArgument(parent: AbstractStellarCommand<*>, name: String, val min: Double, val max: Double) : AbstractStellarArgument<DoubleArgument, Double>(parent, name)