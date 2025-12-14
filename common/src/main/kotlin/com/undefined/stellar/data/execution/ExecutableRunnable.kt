package com.undefined.stellar.data.execution

import com.undefined.stellar.BukkitCtx
import com.undefined.stellar.data.argument.CommandContext
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Suppress("UNCHECKED_CAST")
@ApiStatus.Internal
data class ExecutableRunnable<C : CommandSender>(
    val alwaysApplicable: Boolean,
    val clazz: KClass<C>,
    val runnable: StellarRunnable<C>
) {
    operator fun invoke(context: CommandContext<CommandSender>): CompletableFuture<Boolean> {
        if (clazz.safeCast(context.sender) == null) return CompletableFuture.completedFuture(false)
        return runnable(context as? CommandContext<C> ?: return CompletableFuture.completedFuture(false))
    }

    constructor(
        alwaysApplicable: Boolean,
        clazz: KClass<C>,
        runnable: CommandContext<C>.() -> Boolean,
    ) : this(alwaysApplicable, clazz, StellarRunnable {
        BukkitCtx.scope.async {
            runnable(it)
        }.asCompletableFuture()
    })
}