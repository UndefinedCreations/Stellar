package com.undefined.stellar.argument.misc

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.NamespacedKey

class NamespacedKeyArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<NamespacedKeyArgument, NamespacedKey>(parent, name)