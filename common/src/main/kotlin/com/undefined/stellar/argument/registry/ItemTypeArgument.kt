package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.inventory.ItemType

class ItemTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ItemTypeArgument, ItemType>(parent, name)