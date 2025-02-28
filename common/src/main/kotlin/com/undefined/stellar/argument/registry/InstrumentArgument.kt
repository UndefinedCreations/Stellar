package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.MusicInstrument
import org.bukkit.Registry

class InstrumentArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<MusicInstrument>, MusicInstrument>(parent, name, Registry.INSTRUMENT)