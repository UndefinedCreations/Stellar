package com.undefined.stellar.kotlin

import com.undefined.stellar.StellarConfig
import kotlinx.coroutines.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import kotlin.coroutines.CoroutineContext

object AsyncCtx : CoroutineDispatcher() {

    private val group = ThreadGroup("Async-Coroutine-Executors")

    val executor: ScheduledExecutorService by lazy {
        object :
            ScheduledThreadPoolExecutor(
                8,
                Thread.ofPlatform()
                    .group(group)
                    .name("Async-Coroutine-Executor-", 0)
                    .daemon(true)
                    .uncaughtExceptionHandler { t, e ->
                        StellarConfig.plugin!!.logger.severe("An error occurred while running a task in $t!")
                        e.printStackTrace()
                    }
                    .factory(),
            ) {

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                if (t != null) {
                    StellarConfig.plugin!!.logger.severe("An error occurred while running a task $r!")
                    t.printStackTrace()
                }
            }
        }
    }


    val scope by lazy {
        CoroutineScope(
            this + SupervisorJob() + CoroutineExceptionHandler { _, e ->
                StellarConfig.plugin!!.logger.severe("An error occurred while running a task!")
                e.printStackTrace()
            },
        )
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return !group.parentOf(Thread.currentThread().threadGroup)
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (isDispatchNeeded(context)) {
            executor.execute(block)
        } else {
            block.run()
        }
    }

    operator fun invoke(block: suspend CoroutineScope.() -> Unit) = scope.launch(block = block)
    operator fun invoke() = scope

}