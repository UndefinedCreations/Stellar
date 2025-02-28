package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.block.BlockType

@Suppress("UnstableApiUsage")
class BlockTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<BlockType>, BlockType>(parent, name, Registry.BLOCK)