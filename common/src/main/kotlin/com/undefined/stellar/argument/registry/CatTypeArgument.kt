package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.entity.Cat

class CatTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<CatTypeArgument, Cat.Type>(parent, name)