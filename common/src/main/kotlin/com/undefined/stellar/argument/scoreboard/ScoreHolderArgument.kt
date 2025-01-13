package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class ScoreHolderArgument(parent: AbstractStellarCommand<*>, name: String, val type: com.undefined.stellar.argument.scoreboard.ScoreHolderType) : AbstractStellarArgument<com.undefined.stellar.argument.scoreboard.ScoreHolderArgument>(parent, name)

enum class ScoreHolderType {
    SINGLE,
    MULTIPLE;
}