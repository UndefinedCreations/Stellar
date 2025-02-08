package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.Art

class ArtArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ArtArgument, Art>(parent, name)