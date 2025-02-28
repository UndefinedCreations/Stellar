package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.inventory.ItemType

@Suppress("UnstableApiUsage")
class ItemTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<ItemType>, ItemType>(parent, name, Registry.ITEM)