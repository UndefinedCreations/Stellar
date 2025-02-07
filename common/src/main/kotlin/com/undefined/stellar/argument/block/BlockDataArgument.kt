package com.undefined.stellar.argument.block

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.block.data.BlockData

class BlockDataArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<BlockDataArgument, BlockData>(parent, name)