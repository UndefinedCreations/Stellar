package com.undefined.stellar.argument.entity

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.entity.Entity

/**
 * An argument that allows for the selection of a single, or multiple entities.
 * It also includes the use of target selectors (@a, @e, etc.).
 *
 * You can customize quantity and type of entities that the player can select using the [EntityDisplayType] enum.
 */
class EntityArgument(name: String, val type: EntityDisplayType) : AbstractStellarArgument<EntityArgument, Entity>(name)

/**
 * Dictates which entities are allowed, and the quantity of entities that are allowed.
 */
enum class EntityDisplayType {
    /**
     * Allows you to only select _one_ entity, including players.
     */
    ENTITY,
    /**
     * Allows you to select any number of entities, including player.
     */
    ENTITIES,
    /**
     * Allows you to only select _one_ player, while not allowing entities.
     */
    PLAYER,
    /**
     * Allows you to only select any number of players, while not allowing entities.
     */
    PLAYERS
}