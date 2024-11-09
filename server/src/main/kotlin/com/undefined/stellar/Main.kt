package com.undefined.stellar

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


class Main : JavaPlugin() {

    override fun onEnable() {
        val adventure = BukkitAudiences.create(this);

        StellarCommand("test")
            .addMessageSubCommand("message")
            .addMessageExecution<Player> { component ->
                println(component)
                adventure.player(this).sendMessage(component)
            }
            .register()
    }

}