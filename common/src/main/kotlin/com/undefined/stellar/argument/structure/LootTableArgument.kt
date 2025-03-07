package com.undefined.stellar.argument.structure

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.loot.LootTable
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID

class LootTableArgument(name: String) : AbstractStellarArgument<LootTableArgument, LootTable>(name)