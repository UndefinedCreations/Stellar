package com.undefined.stellar.argument.basic

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class LongArgument(parent: AbstractStellarCommand<*>, name: String, val min: Long, val max: Long) : AbstractStellarArgument<LongArgument, Long>(parent, name)