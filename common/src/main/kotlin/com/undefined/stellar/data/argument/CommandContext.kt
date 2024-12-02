package com.undefined.stellar.data.argument

import java.util.*

typealias CommandNode = LinkedList<Any?>

class CommandContext(val arguments: CommandNode, val input: String) {
    fun <T> getSubCommand(int: Int): T  = arguments[int] as T
    operator fun get(int: Int) = arguments[int]
}