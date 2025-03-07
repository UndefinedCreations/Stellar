package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarArgument

class ScoreHolderArgument(name: String, val type: ScoreHolderType) : AbstractStellarArgument<ScoreHolderArgument, String>(name)

enum class ScoreHolderType {
    SINGLE,
    MULTIPLE;
}