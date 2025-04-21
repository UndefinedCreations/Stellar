package com.undefined.stellar.argument.math

import com.undefined.stellar.ParameterArgument
import org.bukkit.Location

/**
 * An argument that allows you to pass in a valid rotation consisting of yaw and pitch, which returns a [Location] only containing yaw and pitch.
 */
class RotationArgument(name: String) : ParameterArgument<RotationArgument, Location>(name)