package com.undefined.stellar.command

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.sub.BaseStellarSubCommand
import com.undefined.stellar.util.getNMSVersion

fun BaseStellarCommand.register() {
    if (this is BaseStellarSubCommand) return this.parent.register()
    when (getNMSVersion()) {
        "1.20.6" -> com.undefined.stellar.v1_20_6.BrigadierCommandManager.register(this)
        else -> {} // Throw an exception for version not supported
    }
}