package com.undefined.stellar.sub.arguments.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

class DoubleSubCommand(parent: AbstractStellarCommand<*>, name: String, val min: Double, val max: Double) : AbstractStellarSubCommand<DoubleSubCommand>(parent, name)