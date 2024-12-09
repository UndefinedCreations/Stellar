package com.undefined.stellar.argument.types.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

open class ListArgument<T>(
    parent: AbstractStellarCommand<*>,
    name: String,
    val list: () -> List<T>,
    val stringifier: (T) -> String = { it.toString() },
    val parse: (String) -> T?
) : AbstractStellarArgument<ListArgument<T>>(parent, name) {

    constructor(parent: AbstractStellarCommand<*>,
                name: String,
                list: List<T>,
                stringifier: (T) -> String = { it.toString() },
                parse: (String) -> T?) : this(parent, name, { list }, stringifier, parse)

    fun getStringList(): List<String> = list().map(stringifier)

}
