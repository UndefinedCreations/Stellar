package com.undefined.stellar.sub.arguments.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

open class ListSubCommand<T>(
    parent: AbstractStellarCommand<*>,
    name: String,
    val list: () -> List<T>,
    val stringifier: (T) -> String = { it.toString() },
    val parse: (String) -> T?
) : AbstractStellarSubCommand<ListSubCommand<T>>(parent, name) {

    constructor(parent: AbstractStellarCommand<*>,
                name: String,
                list: List<T>,
                stringifier: (T) -> String = { it.toString() },
                parse: (String) -> T?) : this(parent, name, { list }, stringifier, parse)

    fun getStringList(): List<String> = list().map(stringifier)

}
