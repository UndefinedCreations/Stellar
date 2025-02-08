package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.attribute.Attribute

class AttributeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<AttributeArgument, Attribute>(parent, name)