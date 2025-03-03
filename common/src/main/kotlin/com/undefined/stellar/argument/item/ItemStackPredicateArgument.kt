package com.undefined.stellar.argument.item

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.inventory.ItemStack
import java.util.function.Predicate

class ItemStackPredicateArgument(name: String) : AbstractStellarArgument<ItemStackPredicateArgument, Predicate<ItemStack>>(name)