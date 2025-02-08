package com.undefined.stellar.argument.structure

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.loot.LootTable

class LootTableArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<LootTableArgument, LootTable>(parent, name)