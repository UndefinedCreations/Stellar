package com.undefined.stellar.argument.types.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class AttributeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<AttributeArgument>(parent, name)