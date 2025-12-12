package com.undefined.stellar

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import kotlin.coroutines.CoroutineContext

object StellarConfig {

    @ApiStatus.Internal
    var miniMessage: MiniMessage? = null
        private set
        get() = field ?: MiniMessage.miniMessage()

    @ApiStatus.Internal
    var prefix: String = ""
        private set

    @ApiStatus.Internal
    var plugin: JavaPlugin? = null
        private set

    @ApiStatus.Internal
    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()

    private val asyncCtx = object : CoroutineDispatcher() {

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
                            plugin!!.logger.severe("An error occurred while running a task in $t!")
                            e.printStackTrace()
                        }
                        .factory(),
                ) {

                override fun afterExecute(r: kotlinx.coroutines.Runnable?, t: Throwable?) {
                    if (t != null) {
                        plugin!!.logger.severe("An error occurred while running a task $r!")
                        t.printStackTrace()
                    }
                }
            }
        }


        val scope by lazy {
            CoroutineScope(
                this + SupervisorJob() + CoroutineExceptionHandler { _, e ->
                    plugin!!.logger.severe("An error occurred while running a task!")
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

    private var scope: CoroutineScope = asyncCtx.scope
        set(value) {
            field = value
            if (value != asyncCtx.scope) {
                asyncCtx.executor.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS)
            }
        }

    @ApiStatus.Internal
    fun getStellarCommand(name: String): AbstractStellarCommand<*>? =
        commands.firstOrNull { it.name == name.substringAfter(':') || name in it.aliases }

    /**
     * Sets the [MiniMessage] instance in [StellarConfig], which will be used a default value.
     * @return The modified [StellarConfig].
     */
    @JvmStatic
    fun setMiniMessage(miniMessage: MiniMessage): StellarConfig = apply {
        this.miniMessage = miniMessage
    }

    @JvmStatic
    fun setScope(scope: CoroutineScope) = apply {
        this.scope = scope
    }

    @JvmStatic
    fun getScope() = scope

    /**
     * Sets the default prefix in [StellarConfig], which will be used a default value.
     * @return The modified [StellarConfig].
     */
    @JvmStatic
    fun setPrefix(prefix: String): StellarConfig = apply {
        this.prefix = prefix
    }

    /**
     * Sets the [JavaPlugin] instance in [StellarConfig], which will be used a default value.
     * @return The modified [StellarConfig].
     */
    @JvmStatic
    fun setPlugin(plugin: JavaPlugin): StellarConfig = apply {
        this.plugin = plugin
    }


}