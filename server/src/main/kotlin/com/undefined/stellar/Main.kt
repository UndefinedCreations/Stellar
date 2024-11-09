package com.undefined.stellar

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addItemPredicateSubCommand("item")
            .addItemPredicateExecution<Player> {
                println(it.test(ItemStack(Material.PAPER)))
            }
            .register()
    }

}