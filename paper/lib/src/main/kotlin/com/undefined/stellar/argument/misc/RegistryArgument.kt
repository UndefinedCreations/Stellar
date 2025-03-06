package com.undefined.stellar.argument.misc

import com.undefined.stellar.AbstractStellarArgument
import io.papermc.paper.registry.RegistryKey
import org.bukkit.NamespacedKey
import org.bukkit.Registry

@Suppress("UnstableApiUsage", "DEPRECATION")
class RegistryArgument(
    name: String,
    val registry: RegistryKey<*>,
) : AbstractStellarArgument<RegistryArgument, NamespacedKey>(name) {
    companion object {
        val registryNames: Map<RegistryKey<*>, String> = mapOf(
            RegistryKey.GAME_EVENT to "game_event",
            RegistryKey.STRUCTURE_TYPE to "worldgen/structure_type",
            RegistryKey.MOB_EFFECT to "mob_effect",
            RegistryKey.BLOCK to "block",
            RegistryKey.ITEM to "item",
            RegistryKey.CAT_VARIANT to "cat_variant",
            RegistryKey.FROG_VARIANT to "frog_variant",
            RegistryKey.VILLAGER_PROFESSION to "villager_profession",
            RegistryKey.VILLAGER_TYPE to "villager_type",
            RegistryKey.MAP_DECORATION_TYPE to "map_decoration_type",
            RegistryKey.MENU to "menu",
            RegistryKey.ATTRIBUTE to "attribute",
            RegistryKey.FLUID to "fluid",
            RegistryKey.SOUND_EVENT to "sound_event",
            RegistryKey.DATA_COMPONENT_TYPE to "data_component_type",
            RegistryKey.BIOME to "worldgen/biome",
            RegistryKey.STRUCTURE to "worldgen/structure",
            RegistryKey.TRIM_MATERIAL to "trim_material",
            RegistryKey.TRIM_PATTERN to "trim_pattern",
            RegistryKey.DAMAGE_TYPE to "damage_type",
            RegistryKey.WOLF_VARIANT to "wolf_variant",
            RegistryKey.ENCHANTMENT to "enchantment",
            RegistryKey.JUKEBOX_SONG to "jukebox_song",
            RegistryKey.BANNER_PATTERN to "banner_pattern",
            RegistryKey.PAINTING_VARIANT to "painting_variant",
            RegistryKey.INSTRUMENT to "instrument",
            RegistryKey.ENTITY_TYPE to "entity_type",
            RegistryKey.PARTICLE_TYPE to "particle_type",
            RegistryKey.POTION to "potion",
            RegistryKey.MEMORY_MODULE_TYPE to "memory_module_type",
        )
    }
}