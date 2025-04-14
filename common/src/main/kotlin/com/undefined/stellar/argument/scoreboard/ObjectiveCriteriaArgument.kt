package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.scoreboard.Criteria

/**
 * Allows you to pass in an [objective criterion](https://minecraft.wiki/w/Scoreboard#Criteria), returning the criteria name.
 */
class ObjectiveCriteriaArgument(name: String) : AbstractStellarArgument<ObjectiveCriteriaArgument, String>(name)