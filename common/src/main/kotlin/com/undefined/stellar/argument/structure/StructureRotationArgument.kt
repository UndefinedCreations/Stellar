package com.undefined.stellar.argument.structure

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.loot.LootTable
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID

class StructureRotationArgument(name: String) : AbstractStellarArgument<StructureRotationArgument, StructureRotation>(name)