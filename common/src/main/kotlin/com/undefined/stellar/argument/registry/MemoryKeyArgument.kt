package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.entity.memory.MemoryKey

class MemoryKeyArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<MemoryKey<*>>, MemoryKey<*>>(parent, name, Registry.MEMORY_MODULE_TYPE)