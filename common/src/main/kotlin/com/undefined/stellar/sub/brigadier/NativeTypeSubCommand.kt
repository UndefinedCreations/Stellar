package com.undefined.stellar.sub.brigadier

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.AbstractStellarSubCommand

abstract class NativeTypeSubCommand<T>(parent: BaseStellarCommand<*>, name: String) : AbstractStellarSubCommand<T>(parent, name) {
    val customExecutions: MutableList<CustomStellarExecution<*, Any>> = mutableListOf()
    val customRunnables: MutableList<CustomStellarRunnable<*, Any>> = mutableListOf()
}
