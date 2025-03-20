package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.scoreboard.Team

/**
 * An argument that allows you to pass in an unquoted string, which resolves into a [Team].
 * Allowed characters include: -, +, ., _, A-Z, a-z, and 0-9.
 */
class TeamArgument(name: String) : AbstractStellarArgument<TeamArgument, Team>(name)