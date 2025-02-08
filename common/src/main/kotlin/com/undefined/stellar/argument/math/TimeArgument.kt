package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class TimeArgument(parent: AbstractStellarCommand<*>, name: String, val minimum: Int) : AbstractStellarArgument<TimeArgument, Long>(parent, name)