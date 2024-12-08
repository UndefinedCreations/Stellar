package com.undefined.stellar.sub.arguments.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

class FloatSubCommand(parent: AbstractStellarCommand<*>, name: String, val min: Float, val max: Float) : AbstractStellarSubCommand<FloatSubCommand>(parent, name)