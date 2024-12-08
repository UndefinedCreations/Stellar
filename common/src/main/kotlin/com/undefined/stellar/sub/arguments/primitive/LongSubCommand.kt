package com.undefined.stellar.sub.arguments.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

class LongSubCommand(parent: AbstractStellarCommand<*>, name: String, val min: Long, val max: Long) : AbstractStellarSubCommand<LongSubCommand>(parent, name)