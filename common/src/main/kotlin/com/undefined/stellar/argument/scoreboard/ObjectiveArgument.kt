package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.scoreboard.Objective

class ObjectiveArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ObjectiveArgument, Objective>(parent, name)