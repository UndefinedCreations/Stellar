package com.undefined.stellar

import com.undefined.stellar.data.ParticleData
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class Main : JavaPlugin() {

    val players: HashMap<UUID, ParticleData<*>> = hashMapOf()

    override fun onEnable() {
        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            for ((uuid, data) in players) {
                val player = Bukkit.getPlayer(uuid) ?: continue
                player.spawnParticle(data.particle, player.location.add(0.0, -0.5, 0.0), 100, 0.3, 0.0, 0.3, data.options)
            }
        }, 5, 5)

        StellarCommand("test")
            .addParticleSubCommand("particle")
            .addParticleExecution<Player> {
                players[uniqueId] = it
            }
            .register()
    }

}