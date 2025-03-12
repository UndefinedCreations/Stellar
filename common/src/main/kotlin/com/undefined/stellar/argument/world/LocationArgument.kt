package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.Location

class LocationArgument(name: String, val type: LocationType) : AbstractStellarArgument<LocationArgument, Location>(name)

enum class LocationType {
    LOCATION_3D,
    LOCATION_2D,
    PRECISE_LOCATION_3D,
    PRECISE_LOCATION_2D
}