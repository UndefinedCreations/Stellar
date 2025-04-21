package com.undefined.stellar.argument.structure

import com.undefined.stellar.ParameterArgument
import org.bukkit.loot.LootTable

/**
 * An argument that allows you to pass in a valid [SNBT format](https://minecraft.wiki/w/NBT_format#SNBT_format) or a valid loot table key. If the SNBT table doesn't exist, it will create a new one.
 * It returns a [LootTable].
 * @since 1.21.5
 */
class LootTableArgument(name: String) : ParameterArgument<LootTableArgument, LootTable>(name)