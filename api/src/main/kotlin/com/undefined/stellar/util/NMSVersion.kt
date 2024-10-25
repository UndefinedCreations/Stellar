package com.undefined.stellar.util

import org.bukkit.Bukkit

fun getNMSVersion(): String = Bukkit.getBukkitVersion().split("-")[0]
