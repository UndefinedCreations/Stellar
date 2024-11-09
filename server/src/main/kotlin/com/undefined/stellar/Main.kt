package com.undefined.stellar

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


class Main : JavaPlugin() {

    override fun onEnable() {
        val adventure = BukkitAudiences.create(this);

        StellarCommand("test")
            .addColorSubCommand("color")
            .addColorExecution<Player> { color ->
                println(color)
                adventure.player(this).sendMessage(Component.text("text").color(TextColor.color(0xFF0000)).style(color))
            }
            .register()
    }

}