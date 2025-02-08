package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarCommand

class LiteralStellarArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<LiteralStellarArgument, String>(parent, name)