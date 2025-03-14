package com.undefined.stellar.argument.block

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.block.Block
import java.util.function.Predicate

class BlockPredicateArgument(name: String) : AbstractStellarArgument<BlockPredicateArgument, Predicate<Block>>(name)