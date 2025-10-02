package com.undefined.stellar.kotlin

import com.undefined.stellar.StellarConfig
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object KotlinStellarConfig {

    var _scope: CoroutineScope? = null
    val scope: CoroutineScope
        get() = _scope ?: error("Scope has not been set!")

}

fun StellarConfig.setScope(scope: CoroutineScope): StellarConfig = apply {
    KotlinStellarConfig._scope = scope
}

val StellarConfig.scope: CoroutineScope
    get() = KotlinStellarConfig.scope
