package com.undefined.stellar.sub.arguments.world

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

class LocationSubCommand(parent: AbstractStellarCommand<*>, name: String, val type: LocationType) : AbstractStellarSubCommand<LocationSubCommand>(parent, name)

enum class LocationType {
    LOCATION3D,
    LOCATION2D,
    DOUBLE_LOCATION_3D,
    DOUBLE_LOCATION_2D
}
