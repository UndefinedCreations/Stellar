package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.Sound

class SoundArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<SoundArgument, Sound>(parent, name)