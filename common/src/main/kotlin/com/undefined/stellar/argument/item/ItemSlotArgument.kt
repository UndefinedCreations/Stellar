package com.undefined.stellar.argument.item

import com.undefined.stellar.ParameterArgument

/**
 * An argument that returns either an `int` or a [List] of `int`, representing a specific slot in any type of inventory.
 *
 * You can set [multiple] to `false` for the selection of one slot (default), or set [multiple] to true for multiple slots.
 * @since multiple 1.21.5 and single 1.13
 */
class ItemSlotArgument(name: String, val multiple: Boolean = false) : ParameterArgument<ItemSlotArgument, Any>(name)