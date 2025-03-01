package com.undefined.stellar

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.v1_21_4.NMS1_21_4
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object NMSManager {

    val nms: NMS by lazy { versions[version] ?: throw UnsupportedVersionException(versions.keys) }

    private val version by lazy { Bukkit.getBukkitVersion().split("-")[0] }
    private val versions: Map<String, NMS> = mapOf(
        "1.21.4" to NMS1_21_4
    )

    fun register(command: StellarCommand, plugin: JavaPlugin) {
        val builder: LiteralArgumentBuilder<*> = LiteralArgumentBuilder.literal<Any>(command.name)
        nms.register(builder)
    }
}