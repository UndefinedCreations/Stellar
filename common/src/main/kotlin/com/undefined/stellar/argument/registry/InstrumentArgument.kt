package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class InstrumentArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<InstrumentArgument>(parent, name)