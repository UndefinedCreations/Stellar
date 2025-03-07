package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarArgument
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.ChatColor
import org.bukkit.HeightMap
import org.bukkit.Location
import org.bukkit.World

class LocationArgument(name: String, val type: LocationType) : AbstractStellarArgument<LocationArgument, Location>(name)

enum class LocationType {
    LOCATION_3D,
    LOCATION_2D,
    PRECISE_LOCATION_3D,
    PRECISE_LOCATION_2D
}