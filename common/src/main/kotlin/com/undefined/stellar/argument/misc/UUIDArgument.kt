package com.undefined.stellar.argument.misc

import com.undefined.stellar.AbstractStellarArgument
import java.util.*

/**
 * An argument that allows you to pass in a [UUID].
 */
class UUIDArgument(name: String) : AbstractStellarArgument<UUIDArgument, UUID>(name)