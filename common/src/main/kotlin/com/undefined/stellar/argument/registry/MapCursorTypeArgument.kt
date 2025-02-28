package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.map.MapCursor

class MapCursorTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<MapCursor.Type>, MapCursor.Type>(parent, name, Registry.MAP_DECORATION_TYPE)