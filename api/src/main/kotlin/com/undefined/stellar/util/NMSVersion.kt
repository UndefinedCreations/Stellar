package com.undefined.stellar.util

import org.bukkit.Bukkit
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object NMSVersion {
    val version by lazy { Bukkit.getBukkitVersion().split("-")[0] }
}
