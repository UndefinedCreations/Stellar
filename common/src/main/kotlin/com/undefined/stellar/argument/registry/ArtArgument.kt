package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Art
import org.bukkit.Registry

class ArtArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Art>, Art>(parent, name, Registry.ART)