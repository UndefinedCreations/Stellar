@file:Suppress("UNCHECKED_CAST")
@file:ApiStatus.Internal

package com.undefined.stellar.util

import org.jetbrains.annotations.ApiStatus

object ReflectionUtil {
    fun <T : Any> getPrivateField(any: Class<*>, name: String): T =
        any::class.java.getDeclaredField(name).apply { isAccessible = true }[this] as T

    inline fun <reified T : Any, R> getPrivateMethod(name: String, vararg args: Any?): R =
        T::class.java.getDeclaredMethod(name).apply { isAccessible = true }(null, args) as R
}

fun <T : Any> Any.getPrivateField(name: String): T =
    javaClass.getDeclaredField(name).apply { isAccessible = true }[this] as T

fun <T : Any> Any.executePrivateMethod(name: String, vararg args: Any?): T =
    javaClass.getDeclaredMethod(name).apply { isAccessible = true }(this, args) as T