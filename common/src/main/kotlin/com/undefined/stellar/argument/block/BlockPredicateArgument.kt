package com.undefined.stellar.argument.block

import com.undefined.stellar.ParameterArgument
import org.bukkit.block.Block
import java.util.function.Predicate

/**
 * Returns a [Predicate] with the generic type of [Block], using [this syntax](https://minecraft.wiki/w/Argument_types#minecraft:block_predicate).
 * @since 1.13
 */
class BlockPredicateArgument(name: String) : ParameterArgument<BlockPredicateArgument, Predicate<Block>>(name)