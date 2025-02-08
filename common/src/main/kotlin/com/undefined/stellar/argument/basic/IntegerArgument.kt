package com.undefined.stellar.argument.basic

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class IntegerArgument(parent: AbstractStellarCommand<*>, name: String, val min: Int, val max: Int) : AbstractStellarArgument<IntegerArgument, Int>(parent, name)
