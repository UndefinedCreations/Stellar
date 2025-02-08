package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.entity.memory.MemoryKey

class MemoryKeyArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<MemoryKeyArgument, MemoryKey<*>>(parent, name)