package com.undefined.stellar.nms

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Suppress("UNCHECKED_CAST")
object NMSHelper {

    fun getArgumentInput(context: CommandContext<*>, name: String): String? {
        val field = CommandContext::class.java.getDeclaredField("arguments").also { it.isAccessible = true }
        val arguments: Map<String, ParsedArgument<Any, *>> = field.get(context) as Map<String, ParsedArgument<Any, *>>
        val argument = arguments[name] ?: return null
        return argument.range.get(context.input)
    }

    fun getBukkitSender(source: Any): CommandSender = source.javaClass.getDeclaredMethod("getBukkitSender")(source) as CommandSender
    fun hasPermission(player: Player, level: Int): Boolean = player.javaClass.getField("handle")[player].let { it.javaClass.getMethod("hasPermissions").invoke(it, level) as Boolean }

}