package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.Instrument

class InstrumentArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<InstrumentArgument, Instrument>(parent, name)