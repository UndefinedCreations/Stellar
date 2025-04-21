package com.undefined.stellar.argument.misc

import com.undefined.stellar.ParameterArgument
import org.bukkit.NamespacedKey

/**
 * An argument that allows you to pass in a "resource key" (i.e. [NamespacedKey]), with this syntax: `namespace:key`. Returning a [NamespacedKey].
 */
class NamespacedKeyArgument(name: String) : ParameterArgument<NamespacedKeyArgument, NamespacedKey>(name)