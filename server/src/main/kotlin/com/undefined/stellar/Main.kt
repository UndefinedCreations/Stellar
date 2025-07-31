package com.undefined.stellar

import com.undefined.stellar.nms.NMSHelper
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarConfig.setPlugin(this)

        StellarCommand("testing")
            .addExecution<Player> {
                NMSHelper.hasPermission(sender, 1)
            }
            .register()
    }

}