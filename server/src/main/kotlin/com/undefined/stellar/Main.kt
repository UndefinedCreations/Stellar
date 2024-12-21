package com.undefined.stellar

import net.kyori.adventure.text.format.Style
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Predicate

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addColorArgument(name = "item")
            .addExecution<Player> {
                sender.sendMessage(getArgument<Style>("item").toString())
            }
            .register(this)
    }

}