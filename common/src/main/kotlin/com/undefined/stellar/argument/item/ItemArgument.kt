package com.undefined.stellar.argument.item

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.inventory.ItemStack

class ItemArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ItemArgument, ItemStack>(parent, name)