package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class RangeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<RangeArgument, IntRange>(parent, name)