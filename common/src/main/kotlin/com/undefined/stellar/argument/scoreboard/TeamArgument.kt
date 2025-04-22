package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.ParameterArgument
import org.bukkit.scoreboard.Team

/**
 * An argument that allows you to pass in an unquoted string, which resolves into a [Team].
 * Allowed characters include: -, +, ., _, A-Z, a-z, and 0-9.
 * @since 1.13
 */
class TeamArgument(name: String) : ParameterArgument<TeamArgument, Team>(name)