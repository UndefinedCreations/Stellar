package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addLocationArgument("location")
            .addDisplaySlotArgument("string")
            .addExecution<Player> {
                sender.sendMessage(this.getArgument<DisplaySlot>("string").name)
            }
            .addRequirement<Player> {
                this.hasPlayedBefore()
            }
            .register(this)
    }

}