package com.undefined.stellar.data.argument

import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import com.undefined.stellar.argument.entity.EntityAnchorArgument

/**
 * An enum that has two possible values: [FEET] and [EYES]. This argument is used to calculate the offset of a location as if from at the feet or eyes.
 *
 * @param anchorName The name of the anchor as if used in an argument.
 * @param offset A private function providing a location and entity, and returning the location with the respective offset.
 *
 * This is mainly used for the [EntityAnchorArgument].
 */
enum class EntityAnchor(
    val anchorName: String,
    private val offset: (location: Location, entity: LivingEntity?) -> Location
) {

    /**
     * Represents an offset from the feet. This value solely returns the unmodified location.
     */
    FEET("feet", { location, _ -> location }),

    /**
     * Represents an offset from the eyes. This value requires an entity to be present.
     *
     * @throws IllegalArgumentException Whenever a null entity is used.
     */
    EYES("eyes", { location, entity ->
        if (entity == null) throw IllegalArgumentException("The entity cannot be null!")
        Location(
            entity.world,
            location.x,
            location.y + (entity.location.y - entity.eyeLocation.y),
            location.z
        )
    });

    /**
     * Returns the location of the [entity] with the respective offset.
     */
    fun apply(entity: LivingEntity): Location = offset(entity.location, entity)

    /**
     * Returns the [location] and possibly the [entity] with the respective offset. The [entity] can be null, but only if it calculates from the feet, otherwise throws an [IllegalArgumentException].
     *
     * @throws IllegalArgumentException Whenever a null entity is used when it calculates from the eyes.
     */
    fun apply(location: Location, entity: LivingEntity? = null): Location = offset(location, entity)

    companion object {
        /**
         * Gets the feet from the name of the anchor (`feet` or `eyes`).
         *
         * @param ignoreCase Whether to ignore capitalization.
         */
        fun getFromName(name: String, ignoreCase: Boolean = false): EntityAnchor = EntityAnchor.entries.first { it.anchorName.equals(name, ignoreCase) }
    }

}