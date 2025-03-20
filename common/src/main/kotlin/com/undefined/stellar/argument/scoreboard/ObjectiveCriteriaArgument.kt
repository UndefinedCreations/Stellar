package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarArgument

/**
 * Allows you to pass in an [objective criterion](https://minecraft.wiki/w/Scoreboard#Criteria). Returning the name of the criterion, in [String] format.
 */
class ObjectiveCriteriaArgument(name: String) : AbstractStellarArgument<ObjectiveCriteriaArgument, String>(name)