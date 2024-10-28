package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class EnumSubCommand<T : Enum<*>>(parent: BaseStellarCommand, name: String) : NativeTypeSubCommand(parent, name) {
    inline fun <reified T : CommandSender> addEnumExecution(noinline execution: T.(Boolean) -> Unit): BaseStellarCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
