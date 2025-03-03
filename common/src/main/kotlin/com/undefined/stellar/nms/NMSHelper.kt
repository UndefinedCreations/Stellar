package com.undefined.stellar.nms

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.context.StringRange

@Suppress("UNCHECKED_CAST")
object NMSHelper {
    fun getArgumentInput(context: CommandContext<*>, name: String): String? {
        val field = CommandContext::class.java.getDeclaredField("arguments").also { it.isAccessible = true }
        val arguments: Map<String, ParsedArgument<Any, *>> = field.get(context) as Map<String, ParsedArgument<Any, *>>
        val argument = arguments[name] ?: return null
        val range = StringRange.between(argument.range.start, context.input.length)
        return range.get(context.input)
    }
}