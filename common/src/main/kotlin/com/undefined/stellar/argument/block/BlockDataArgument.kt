package com.undefined.stellar.argument.block

import com.undefined.stellar.ParameterArgument
import org.bukkit.block.data.BlockData

/**
 * Returns [BlockData], with this syntax: block_id&#91;block_states&#93;{data_tags}. For more information on the syntax, visit the [wiki](https://minecraft.wiki/w/Argument_types#minecraft:block_state).
 * @since 1.13
 */
class BlockDataArgument(name: String) : ParameterArgument<BlockDataArgument, BlockData>(name)