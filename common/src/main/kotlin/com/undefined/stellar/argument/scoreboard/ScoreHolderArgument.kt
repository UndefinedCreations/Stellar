package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class ScoreHolderArgument(parent: AbstractStellarCommand<*>, name: String, val type: ScoreHolderType) : AbstractStellarArgument<ScoreHolderArgument, String>(parent, name)

enum class ScoreHolderType {
    SINGLE,
    MULTIPLE;
}