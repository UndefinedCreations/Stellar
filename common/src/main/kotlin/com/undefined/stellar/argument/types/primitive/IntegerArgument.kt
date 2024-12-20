package com.undefined.stellar.argument.types.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class IntegerArgument(parent: AbstractStellarCommand<*>, name: String, val min: Int, val max: Int) : AbstractStellarArgument<IntegerArgument>(parent, name)
