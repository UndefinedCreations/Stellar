package com.undefined.stellar.argument.item

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.inventory.ItemStack

/**
 * An argument that returns an [ItemStack], which people can modify by specifying its material and item components (<item_id>[<list of components>]).
 */
class ItemStackArgument(name: String) : AbstractStellarArgument<ItemStackArgument, ItemStack>(name)