package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.attribute.Attribute

class AttributeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Attribute>, Attribute>(parent, name, Registry.ATTRIBUTE)