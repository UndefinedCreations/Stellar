package com.undefined.stellar.data.argument

import org.bukkit.command.CommandSender
import java.lang.IndexOutOfBoundsException

typealias GreedyCommandNode = List<String>

@Suppress("UNUSED")
class PhraseCommandContext<T : CommandSender>(val globalContext: CommandContext<T>, val arguments: GreedyCommandNode, val sender: T, val input: String) {
    fun getArgument(index: Int): String = arguments[index]
    fun getArgumentOrNull(index: Int): String? = try { arguments[index] } catch (e: IndexOutOfBoundsException) { null }

    inline fun <reified T> getGlobalArgument(name: String): T = globalContext.getArgument(name)
    inline fun <reified T> getGlobalArgument(index: Int): T = globalContext.getArgument(index)
    inline fun <reified T> getGlobalArgumentOrNull(name: String): T? = globalContext.getOrNull(name)
    inline fun <reified T> getGlobalArgumentOrNull(index: Int): T? = globalContext.getOrNull(index)

    operator fun get(index: Int) = arguments[index]
}