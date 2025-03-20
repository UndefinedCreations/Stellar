package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.scoreboard.Objective

/**
 * An argument that allows you to pass in a single word (Allowed characters include: -, +, ., _, A-Z, a-z, and 0-9).
 * It resolves into a scoreboard objective during command execution.
 */
class ObjectiveArgument(name: String) : AbstractStellarArgument<ObjectiveArgument, Objective>(name)