package com.undefined.stellar.argument.block

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.block.Block
import java.util.function.Predicate

/**
 * Returns a [Predicate] with the generic type of [Block], using [this syntax](https://minecraft.wiki/w/Argument_types#minecraft:block_predicate).
 */
class BlockPredicateArgument(name: String) : AbstractStellarArgument<BlockPredicateArgument, Predicate<Block>>(name)