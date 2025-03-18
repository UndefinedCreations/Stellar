package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that allows you to pass in a yaw angle, in floating-point number, ranging from -180.0 (north) to 179.9 (just west of north), returning a `float`.
 */
class AngleArgument(name: String) : AbstractStellarArgument<AngleArgument, Float>(name)