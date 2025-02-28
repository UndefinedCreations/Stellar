package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.inventory.MenuType

@Suppress("UnstableApiUsage")
class InventoryTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<MenuType>, MenuType>(parent, name, Registry.MENU)