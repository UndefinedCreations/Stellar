package com.undefined.stellar

import com.undefined.stellar.data.ParticleData
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class Main : JavaPlugin() {

    val players: HashMap<UUID, ParticleData<*>> = hashMapOf()

    override fun onEnable() {
        StellarCommand("test")
            .addRotationSubCommand("rotation")
            .addRotationExecution<Player> {
                location.pitch = it.pitch
                location.yaw = it.yaw
                sendMessage(location.toString())
                sendMessage(it.toString())
            }
            .register()
    }

}