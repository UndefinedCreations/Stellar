package com.undefined.stellar.kotlin

import com.undefined.stellar.StellarConfig
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object KotlinStellarConfig {

    private var _scope: CoroutineScope? = null
    var scope: CoroutineScope
        get() = _scope ?: error("Scope has not been set!")
        set(value) {
            _scope = value
        }


    private var _asyncScope: CoroutineScope? = null
    var asyncScope: CoroutineScope
        get() = _asyncScope ?: _scope ?: error("Neither the async scope or the scope has been set!")
        set(value) {
            _asyncScope = value
        }

}

fun StellarConfig.setScope(scope: CoroutineScope): StellarConfig = apply {
    KotlinStellarConfig.scope = scope
}

fun StellarConfig.setAsyncScope(scope: CoroutineScope): StellarConfig = apply {
    KotlinStellarConfig.asyncScope = scope
}

/**
 * [CoroutineScope] used for any executions or blocks that have to be run in suspend.
 */
val StellarConfig.scope: CoroutineScope
    get() = KotlinStellarConfig.scope

/**
 * [CoroutineScope] used for any executions or blocks that are run in async that have to be run in suspend.
 */
val StellarConfig.asyncScope: CoroutineScope
    get() = KotlinStellarConfig.asyncScope
