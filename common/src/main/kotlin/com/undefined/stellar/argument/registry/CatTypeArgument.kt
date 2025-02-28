package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.entity.Cat

class CatTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Cat.Type>, Cat.Type>(parent, name, Registry.CAT_VARIANT)