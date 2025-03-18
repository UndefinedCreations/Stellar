package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarArgument
import com.undefined.stellar.data.argument.Operation

/**
 * An argument that allows you to pass an arithmetic operator, such as `=` or `+=`.
 * It returns an [Operation], and use the `apply` method to calculate the new value from two numbers.
 */
class OperationArgument(name: String) : AbstractStellarArgument<OperationArgument, Operation>(name)