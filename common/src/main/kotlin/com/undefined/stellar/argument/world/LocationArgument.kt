package com.undefined.stellar.argument.world

import com.undefined.stellar.ParameterArgument
import org.bukkit.Location

/**
 * An argument that allows you to pass in a [Location], as dictated by the [LocationType].
 */
class LocationArgument(name: String, val type: LocationType) : ParameterArgument<LocationArgument, Location>(name)

/**
 * Dictates what kind of location can be selected.
 */
enum class LocationType {
    /**
     * Must be a block position composed of <X>, <Y> and <Z>, each of which must be an integer or a [tilde and caret notation](https://minecraft.wiki/w/Coordinates#Commands).
     */
    LOCATION_3D,

    /**
     * Must be a column coordinates composed of <X> and <Z>, each of which must be an integer or [tilde notation](https://minecraft.wiki/w/Coordinates#Relative_world_coordinates).
     */
    LOCATION_2D,

    /**
     * Must be three-dimensional coordinates with [double-precision floating-point](https://en.wikipedia.org/wiki/Double_precision_floating-point_format) number elements.
     * Accepts [tilde and caret notations](https://minecraft.wiki/w/Coordinates#Commands).
     */
    PRECISE_LOCATION_3D,

    /**
     * Must be two-dimensional coordinates with [double-precision floating-point](https://en.wikipedia.org/wiki/Double_precision_floating-point_format) number elements.
     * Accepts [tilde notation](https://minecraft.wiki/w/Coordinates#Relative_world_coordinates).
     */
    PRECISE_LOCATION_2D
}