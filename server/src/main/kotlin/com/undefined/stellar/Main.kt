package com.undefined.stellar

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        if (Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective("test") == null)
            Bukkit.getScoreboardManager()!!.mainScoreboard.registerNewObjective("test", Criteria.HEALTH, "test")
        StellarCommand("objective")
            .addScoreHolderArgument(name = "holder")
            .addExecution<Player> {
                val holder = getArgument<String>("holder")
                val item = ItemStack(Material.DIAMOND)
                Bukkit.getPlayer(holder)?.inventory?.addItem(item)
            }
            .register(this)
    }

}