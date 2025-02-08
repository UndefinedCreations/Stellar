package com.undefined.stellar.argument.basic

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class BooleanArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<BooleanArgument, Boolean>(parent, name)