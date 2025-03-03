package com.undefined.stellar.data.argument

import org.bukkit.Location
import org.bukkit.entity.LivingEntity

enum class EntityAnchor(
    val anchorName: String,
    private val offset: (location: Location, entity: LivingEntity) -> Location
) {
    FEET("feet", { location, _ -> location }),
    EYES("eyes", { location, entity ->
        Location(
            entity.world,
            location.x,
            location.y + (entity.location.y - entity.eyeLocation.y),
            location.z
        )
    });

    fun apply(entity: LivingEntity): Location = offset(entity.location, entity)
    fun apply(location: Location, entity: LivingEntity): Location = offset(location, entity)

    companion object {
        fun getFromName(name: String, ignoreCase: Boolean = false): EntityAnchor = EntityAnchor.entries.first { it.anchorName.equals(name, ignoreCase) }
    }

}