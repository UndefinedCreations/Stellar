package com.undefined.stellar.argument.text

import com.undefined.stellar.ParameterArgument
import org.bukkit.ChatColor

/**
 * An argument that allows you to pass in a valid team color, returning [ChatColor].
 * @since 1.13
 */
class ColorArgument(name: String) : ParameterArgument<ColorArgument, ChatColor>(name)