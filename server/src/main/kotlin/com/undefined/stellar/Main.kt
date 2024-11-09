package com.undefined.stellar

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


class Main : JavaPlugin() {

    override fun onEnable() {
        val adventure = BukkitAudiences.create(this);

        StellarCommand("test")
            .addObjectiveCriteriaSubCommand("objective")
            .addObjectiveExecution<Player> { component ->
                sendMessage(component)
            }
            .register()
    }

}