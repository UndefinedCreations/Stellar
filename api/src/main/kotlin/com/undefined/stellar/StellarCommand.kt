package com.undefined.stellar

import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.util.getNMSVersion

class StellarCommand(name: String) : BaseStellarCommand(name) {
    override fun register() = when (getNMSVersion()) {
        "1.20.6" -> com.undefined.stellar.v1_20_6.BrigadierCommandManager.register(this)
        else -> throw UnsupportedVersionException()
    }
}