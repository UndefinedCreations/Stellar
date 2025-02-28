package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.basic.ListArgument
import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.command.CommandSender

open class RegistryArgument<R : Registry<K>, K : Keyed>(
    parent: AbstractStellarCommand<*>,
    name: String,
    val registry: R,
    converter: CommandContext<CommandSender>.(K) -> Suggestion = { Suggestion.withText(it.key.toString()) },
    async: Boolean = false
) : ListArgument<K, NamespacedKey>(
    NamespacedKeyArgument(parent, name),
    registry,
    converter,
    { registry.get(it) },
    async
)