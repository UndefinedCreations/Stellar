package com.undefined.stellar.argument.misc

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class NamespacedKeyArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<NamespacedKeyArgument>(parent, name)