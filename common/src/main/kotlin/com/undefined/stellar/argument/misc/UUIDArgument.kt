package com.undefined.stellar.argument.misc

import com.undefined.stellar.AbstractStellarArgument
import java.util.*

/**
 * An argument that allows you to pass in a [UUID].
 *
 * @since 1.16
 */
class UUIDArgument(name: String) : AbstractStellarArgument<UUIDArgument, UUID>(name)