package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarArgument
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.loot.LootTable
import org.bukkit.scoreboard.DisplaySlot
import java.util.UUID

class MessageArgument(name: String) : AbstractStellarArgument<MessageArgument, Component>(name)