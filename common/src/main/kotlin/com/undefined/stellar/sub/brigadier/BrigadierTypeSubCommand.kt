package com.undefined.stellar.sub.brigadier

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.AbstractStellarSubCommand

abstract class BrigadierTypeSubCommand<T>(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarSubCommand<T>(parent, name) {
    val customExecutions: MutableList<CustomStellarExecution<*, Any>> = mutableListOf()
    val customRunnables: MutableList<CustomStellarRunnable<*, Any>> = mutableListOf()
}
