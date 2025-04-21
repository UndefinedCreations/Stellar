package com.undefined.stellar.argument.item

import com.undefined.stellar.ParameterArgument
import org.bukkit.inventory.ItemStack
import java.util.function.Predicate

/**
 * Returns [Predicate] of [ItemStack], which people can modify by specifying its material and item components (<item_type>[<list of tests>]).
 * For more information on the syntax, see the [wiki](https://minecraft.wiki/w/Argument_types#minecraft:item_predicate).
 */
class ItemStackPredicateArgument(name: String) : ParameterArgument<ItemStackPredicateArgument, Predicate<ItemStack>>(name)