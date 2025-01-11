package com.undefined.stellar.exception

import com.undefined.stellar.argument.AbstractStellarArgument
import java.lang.RuntimeException

class UnsupportedArgumentTypeException(val argument: AbstractStellarArgument<*>) : RuntimeException("List argument does not support ${argument::class.simpleName}!")