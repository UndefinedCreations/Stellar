package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.ChatColor

class ColorArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ColorArgument, ChatColor>(parent, name)