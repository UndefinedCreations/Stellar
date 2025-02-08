package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.block.BlockType

class BlockTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<BlockTypeArgument, BlockType>(parent, name)