package com.undefined.stellar.argument.scoreboard

import com.undefined.stellar.ParameterArgument

/**
 * An argument that allows you to pass in one [target](https://minecraft.wiki/w/Argument_types#minecraft:score_holder), or multiple.
 * Returns either a [List] of [String] or one [String] representing a score holder, being a player's name or an entity's UUID that has scores in an objective.
 * The player name doesn’t need to belong to an actual player.
 * @since 1.13
 */
class ScoreHolderArgument(name: String, val type: ScoreHolderType) : ParameterArgument<ScoreHolderArgument, String>(name)

/**
 * This class dictates how many score holders can be selected.
 */
enum class ScoreHolderType {
    /**
     * Allows you to only select _one_ score holder.
     */
    SINGLE,

    /**
     * Allows you to select any number of score holders.
     */
    MULTIPLE;
}