package com.undefined.stellar

import com.undefined.stellar.data.argument.ParticleData
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("spawn-particle")
            .addParticleArgument("particle")
            .addExecution<Player> {
                println("getting data...")
                val data = getArgument<ParticleData<*>>("particle")
                println("data gotten: ${data.particle.name} and ${data.options?.let { data.options } ?: "null"}")
                sender.spawnParticle(data.particle, sender.eyeLocation, 10, 1.0, 1.0, 1.0, data.options)
                println("spawned particle")
            }
            .register(this)
    }

}