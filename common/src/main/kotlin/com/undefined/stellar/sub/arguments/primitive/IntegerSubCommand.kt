package com.undefined.stellar.sub.arguments.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

class IntegerSubCommand(parent: AbstractStellarCommand<*>, name: String, val min: Int, val max: Int) : AbstractStellarSubCommand<IntegerSubCommand>(parent, name)
