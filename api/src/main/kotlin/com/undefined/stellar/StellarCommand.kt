package com.undefined.stellar

import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.util.NMSVersion

private val registrars = mapOf(
    "1.20.6" to com.undefined.stellar.v1_20_6.BrigadierCommandRegistrar
)

class StellarCommand(name: String) : BaseStellarCommand(name) {
    override fun register() {
        val registrar = registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
        registrar.register(this)
    }
}