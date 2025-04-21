package com.undefined.stellar.argument.misc

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.ParameterArgument
import org.bukkit.Registry

/**
 * @since 1.14
 */
@Suppress("UnstableApiUsage")
class RegistryArgument(
    name: String,
    val registry: Registry<*>,
) : ParameterArgument<RegistryArgument, Any>(name) {
    companion object {
        val registryNames: Map<Registry<*>, String> = mapOf(
            Registry.GAME_EVENT to "game_event",
            Registry.STRUCTURE_TYPE to "worldgen/structure_type",
            Registry.EFFECT to "mob_effect",
            Registry.BLOCK to "block",
            Registry.ITEM to "item",
            Registry.CAT_VARIANT to "cat_variant",
            Registry.FROG_VARIANT to "frog_variant",
            Registry.VILLAGER_PROFESSION to "villager_profession",
            Registry.VILLAGER_TYPE to "villager_type",
            Registry.MAP_DECORATION_TYPE to "map_decoration_type",
            Registry.MENU to "menu",
            Registry.ATTRIBUTE to "attribute",
            Registry.FLUID to "fluid",
            Registry.SOUNDS to "sound_event",
            Registry.BIOME to "worldgen/biome",
            Registry.STRUCTURE to "worldgen/structure",
            Registry.TRIM_MATERIAL to "trim_material",
            Registry.TRIM_PATTERN to "trim_pattern",
            Registry.DAMAGE_TYPE to "damage_type",
            Registry.WOLF_VARIANT to "wolf_variant",
            Registry.ENCHANTMENT to "enchantment",
            Registry.JUKEBOX_SONG to "jukebox_song",
            Registry.BANNER_PATTERN to "banner_pattern",
            Registry.ART to "painting_variant",
            Registry.INSTRUMENT to "instrument",
            Registry.ENTITY_TYPE to "entity_type",
            Registry.PARTICLE_TYPE to "particle_type",
            Registry.POTION to "potion",
            Registry.MEMORY_MODULE_TYPE to "memory_module_type",
        )
    }
}

/**
 * Adds a [RegistryArgument] to the command with the given name and [Registry].
 * @return The created [RegistryArgument].
 */
fun AbstractStellarCommand<*>.addRegistryArgument(name: String, registry: Registry<*>): RegistryArgument = addArgument(RegistryArgument(name, registry))