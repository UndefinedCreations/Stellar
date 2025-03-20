package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.HeightMap

/**
 * An argument that allows you to pass in one of the following: `world_surface`, `motion_blocking`, `motion_blocking_no_leaves`, and `ocean_floor`, returning [HeightMap].
 */
class HeightMapArgument(name: String) : AbstractStellarArgument<HeightMapArgument, HeightMap>(name)