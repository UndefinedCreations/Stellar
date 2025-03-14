package com.undefined.stellar.argument.item

import com.undefined.stellar.AbstractStellarArgument

class ItemSlotArgument(name: String, val multiple: Boolean = false) : AbstractStellarArgument<ItemSlotArgument, Int>(name)