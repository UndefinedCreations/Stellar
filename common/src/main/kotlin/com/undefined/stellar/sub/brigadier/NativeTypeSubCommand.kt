package com.undefined.stellar.sub.brigadier

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.CustomStellarExecution
import com.undefined.stellar.sub.BaseStellarSubCommand

abstract class NativeTypeSubCommand(parent: BaseStellarCommand, name: String) : BaseStellarSubCommand(parent, name) {
    val customExecutions: MutableList<CustomStellarExecution<*, Any>> = mutableListOf()

//    fun addCustomExecute
}
