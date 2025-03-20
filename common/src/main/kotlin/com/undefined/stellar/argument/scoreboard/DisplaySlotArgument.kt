package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.scoreboard.DisplaySlot

/**
 * An argument that allows you to pass in a [DisplaySlot] (e.g. `sidebar` or `below_name`).
 */
class DisplaySlotArgument(name: String) : AbstractStellarArgument<DisplaySlotArgument, DisplaySlot>(name)