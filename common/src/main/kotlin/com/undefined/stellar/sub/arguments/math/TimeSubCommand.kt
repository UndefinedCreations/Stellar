package com.undefined.stellar.sub.arguments.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

class TimeSubCommand(parent: AbstractStellarCommand<*>, name: String, val minimum: Int) : AbstractStellarSubCommand<TimeSubCommand>(parent, name)