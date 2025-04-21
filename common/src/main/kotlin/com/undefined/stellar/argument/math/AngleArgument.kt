package com.undefined.stellar.argument.math

import com.undefined.stellar.ParameterArgument

/**
 * An argument that allows you to pass in a yaw angle, in floating-point number, ranging from -180.0 (north) to 179.9 (just west of north), returning a `float`.
 *
 * @since 1.16.2
 */
class AngleArgument(name: String) : ParameterArgument<AngleArgument, Float>(name)