package com.undefined.stellar

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


class Main : JavaPlugin() {

    override fun onEnable() {
        val adventure = BukkitAudiences.create(this);

        StellarCommand("test")
            .addStyleSubCommand("color")
            .addStyleExecution<Player> { component ->
                println(component)
                adventure.player(this).sendMessage(Component.text("test").style(component))
            }
            .register()
    }

}