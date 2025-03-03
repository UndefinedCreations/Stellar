package com.undefined.stellar.argument.list

import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.command.CommandSender

class RegistryArgument<R : Registry<K>, K : Keyed>(
    name: String,
    val registry: R,
    converter: CommandSender.(K) -> Suggestion = { Suggestion.withText(it.key.toString()) },
    async: Boolean = false
) : ListArgument<K, NamespacedKey>(
    NamespacedKeyArgument(name),
    registry,
    converter,
    { registry.get(it) },
    async
) {
    override fun getSuggestionList(context: CommandContext<CommandSender>, input: String): List<Suggestion> =
        list(context).mapNotNull { converter(context.sender, it) }.filter { it.text.substringAfter(':').startsWith(input, true) }
}