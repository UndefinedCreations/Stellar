package com.undefined.stellar.argument.item

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.inventory.ItemStack
import java.util.function.Predicate

class ItemPredicateArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ItemPredicateArgument, Predicate<ItemStack>>(parent, name)