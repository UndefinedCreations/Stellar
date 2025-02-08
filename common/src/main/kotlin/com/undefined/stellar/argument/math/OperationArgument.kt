package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.argument.Operation

class OperationArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<OperationArgument, Operation>(parent, name)