package com.undefined.stellar.argument.block

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.block.Block
import java.util.function.Predicate

class BlockPredicateArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<BlockPredicateArgument, Predicate<Block>>(parent, name)