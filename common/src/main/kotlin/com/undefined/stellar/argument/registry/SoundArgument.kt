package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.Sound

class SoundArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Sound>, Sound>(parent, name, Registry.SOUNDS)