package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.ChatColor

/**
 * An argument that allows you to pass in a valid team color, returning [ChatColor].
 */
class ColorArgument(name: String) : AbstractStellarArgument<ColorArgument, ChatColor>(name)