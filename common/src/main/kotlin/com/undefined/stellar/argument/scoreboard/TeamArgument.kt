package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.scoreboard.Team

class TeamArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<TeamArgument, Team>(parent, name)