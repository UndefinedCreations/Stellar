package com.undefined.stellar

import com.mojang.brigadier.builder.LiteralArgumentBuilder

interface NMS {
    fun register(command: LiteralArgumentBuilder<*>)
}