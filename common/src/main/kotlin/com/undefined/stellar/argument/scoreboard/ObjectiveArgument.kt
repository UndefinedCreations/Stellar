package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import java.util.UUID

class ObjectiveArgument(name: String) : AbstractStellarArgument<ObjectiveArgument, Objective>(name)