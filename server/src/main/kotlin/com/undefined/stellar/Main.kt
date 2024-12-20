package com.undefined.stellar

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Predicate

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("isItem")
            .addItemArgument(name = "item")
            .addExecution<Player> {
                val predicate = getArgument<Predicate<ItemStack>>("item")
                sender.inventory.setItem()
            }
            .register(this)
    }

}