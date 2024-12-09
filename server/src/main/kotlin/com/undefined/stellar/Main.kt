package com.undefined.stellar

import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addDamageTypeArgument("type")
            .addExecution<Player> {
                source.sendMessage(getArgument<DamageType>("type").deathMessageType.name)
            }
            .addInventoryTypeArgument("type")
            .addExecution<Player> {
                source.sendMessage(getArgument<InventoryType>("type").name)
            }
            .register(this)
    }

}