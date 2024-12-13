package com.undefined.stellar.data.argument

import org.bukkit.command.CommandSender

typealias GreedyCommandNode = List<String>

@Suppress("UNCHECKED_CAST", "UNUSED")
class GreedyCommandContext<T : CommandSender>(val arguments: GreedyCommandNode, val sender: T, val input: String) {
    fun getArgument(index: Int): String = arguments[index]
    operator fun get(index: Int) = arguments[index]
}