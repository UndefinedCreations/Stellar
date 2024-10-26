package com.undefined.stellar.util

import org.bukkit.Bukkit

object NMSVersion {
    val version by lazy { Bukkit.getBukkitVersion().split("-")[0] }
}
