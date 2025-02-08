package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.event.inventory.InventoryType

class InventoryTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<InventoryTypeArgument, InventoryType>(parent, name)