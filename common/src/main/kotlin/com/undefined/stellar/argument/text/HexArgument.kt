package com.undefined.stellar.argument.text

import com.undefined.stellar.ParameterArgument
import org.bukkit.ChatColor

/**
 * An argument that allows you to pass in a valid team color, returning an integer.
 * @since 1.21.6
 */
class HexArgument(name: String) : ParameterArgument<HexArgument, Int>(name)