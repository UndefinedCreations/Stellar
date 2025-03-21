package com.undefined.stellar.data.failure

/**
 * A data class with the possibility to hide the default Minecraft failure messages.
 *
 * @param hide Whether to hide the default failure messages.
 * @param global Whether it should also be applied for all subsequent arguments.
 */
data class HideDefaultFailureMessages(val hide: Boolean, val global: Boolean)