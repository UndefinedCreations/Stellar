package com.undefined.stellar.argument.types.world

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class LocationArgument(parent: AbstractStellarCommand<*>, name: String, val type: LocationType) : AbstractStellarArgument<LocationArgument>(parent, name)

enum class LocationType {
    LOCATION_3D,
    LOCATION_2D,
    PRECISE_LOCATION_3D,
    PRECISE_LOCATION_2D
}