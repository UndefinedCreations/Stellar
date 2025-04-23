package com.undefined.stellar.data.cooldown

import com.undefined.stellar.data.argument.CommandContext
import org.bukkit.entity.Player

/**
 * Represents a functional interface used for command cooldowns.
 */
fun interface CooldownExecution {
    operator fun invoke(context: CommandContext<Player>, remaining: Long)
}