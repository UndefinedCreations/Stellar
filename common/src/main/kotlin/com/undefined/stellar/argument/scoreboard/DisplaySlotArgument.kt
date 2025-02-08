package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.scoreboard.DisplaySlot

class DisplaySlotArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<DisplaySlotArgument, DisplaySlot>(parent, name)