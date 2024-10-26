package com.undefined.stellar.sub

import com.undefined.stellar.BaseStellarCommand

class BaseStellarSubCommand(val parent: BaseStellarCommand, name: String) : BaseStellarCommand(name) {
    override fun register() = parent.register()
}