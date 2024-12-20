package com.undefined.stellar.argument.types.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class MemoryKeyArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<MemoryKeyArgument>(parent, name)