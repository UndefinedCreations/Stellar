package com.undefined.stellar.sub.brigadier.world

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import java.util.function.Predicate

@Suppress("UNCHECKED_CAST")
class BlockPredicateSubCommand(parent: BaseStellarCommand<*>, name: String) : NativeTypeSubCommand<BlockPredicateSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addBlockPredicateExecution(noinline execution: T.(Predicate<Block>) -> Unit): BlockPredicateSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunBlockPredicate(noinline execution: T.(Predicate<Block>) -> Boolean): BlockPredicateSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
