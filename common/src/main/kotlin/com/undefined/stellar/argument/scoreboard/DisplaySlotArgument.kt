package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.ParameterArgument
import org.bukkit.scoreboard.DisplaySlot

/**
 * An argument that allows you to pass in a [DisplaySlot] (e.g. `sidebar` or `below_name`).
 * @since 1.13
 */
class DisplaySlotArgument(name: String) : ParameterArgument<DisplaySlotArgument, DisplaySlot>(name)