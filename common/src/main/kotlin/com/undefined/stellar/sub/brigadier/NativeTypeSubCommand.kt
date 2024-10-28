package com.undefined.stellar.sub.brigadier

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.BaseStellarSubCommand

abstract class NativeTypeSubCommand<T>(parent: BaseStellarCommand<*>, name: String) : BaseStellarSubCommand<T>(parent, name) {
    val customExecutions: MutableList<CustomStellarExecution<*, Any>> = mutableListOf()
}
