package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffectType

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addPotionEffectTypeArgument("type")
            .addExecution<Player> {
                source.sendMessage(getArgument<PotionEffectType>("type").key.toString())
            }
            .register(this)
    }

}