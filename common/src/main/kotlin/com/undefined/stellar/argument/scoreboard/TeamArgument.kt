package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Team
import java.util.UUID

class TeamArgument(name: String) : AbstractStellarArgument<TeamArgument, Team>(name)