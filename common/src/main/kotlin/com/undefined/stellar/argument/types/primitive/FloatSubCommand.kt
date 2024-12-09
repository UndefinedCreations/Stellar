package com.undefined.stellar.argument.types.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class FloatArgument(parent: AbstractStellarCommand<*>, name: String, val min: Float, val max: Float) : AbstractStellarArgument<FloatArgument>(parent, name)