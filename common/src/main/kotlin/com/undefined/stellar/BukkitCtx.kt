package com.undefined.stellar

import kotlinx.coroutines.*
import org.bukkit.Bukkit
import kotlin.coroutines.CoroutineContext

object BukkitCtx : CoroutineDispatcher() {

    val scope by lazy {
        CoroutineScope(
            this + SupervisorJob() + CoroutineExceptionHandler { _, e ->
                StellarConfig.plugin!!.logger.severe("An error occurred while running a task!")
                e.printStackTrace()
            },
        )
    }
    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !Bukkit.getServer().isPrimaryThread
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (isDispatchNeeded(context)) {
            Bukkit.getScheduler().runTask(StellarConfig.plugin!!, Runnable {
                try {
                    block.run()
                } catch (e: Throwable) {
                    StellarConfig.plugin!!.logger.severe("An error occurred while running a task!")
                    e.printStackTrace()
                }
            })
        } else {
            block.run()
        }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = block)
    }

    operator fun invoke(block: suspend CoroutineScope.() -> Unit) = launch(block = block)
    operator fun invoke() = scope
}