package com.undefined.stellar.argument.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class LongArgument(parent: AbstractStellarCommand<*>, name: String, val min: Long, val max: Long) : AbstractStellarArgument<com.undefined.stellar.argument.primitive.LongArgument>(parent, name)