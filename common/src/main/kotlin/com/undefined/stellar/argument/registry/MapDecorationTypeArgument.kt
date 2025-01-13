package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.world.LocationArgument

class MapDecorationTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<LocationArgument>(parent, name)
