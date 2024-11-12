package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.loot.LootContext
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random
import kotlin.random.asJavaRandom

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addLootTableSubCommand("test")
            .addLootTableExecution<Player> {
                it.fillInventory(inventory, Random.asJavaRandom(), LootContext.Builder(location).build())
                println(it.populateLoot(Random.asJavaRandom(), LootContext.Builder(location).build()).map { it.type })
            }
            .register()
    }

}