package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.ParameterArgument

/**
 * Allows you to pass in an [objective criterion](https://minecraft.wiki/w/Scoreboard#Criteria), returning the criteria name.
 */
class ObjectiveCriteriaArgument(name: String) : ParameterArgument<ObjectiveCriteriaArgument, String>(name)