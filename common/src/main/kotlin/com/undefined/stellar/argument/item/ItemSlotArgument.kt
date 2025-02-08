package com.undefined.stellar.argument.item

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class ItemSlotArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ItemSlotArgument, Int>(parent, name)