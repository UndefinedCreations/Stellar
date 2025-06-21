package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.argument.misc.UUIDArgument
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarConfig.setPlugin(this)

        StellarCommand("color")
            .addHexArgument("hex")
            .addExecution<Player> {
                val hex: Int by args
                sender.sendMessage("You chose this hex: ${String.format("%06X", hex)}")
            }
            .register()
    }

}