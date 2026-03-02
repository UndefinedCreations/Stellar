package com.undefined.stellar.command

/**
 * @param C Command context.
 */
interface Command<C : Any> {
    val name: String
//    fun argument(argument: CommandArgument<C>)
//    fun execute(block: C.() -> Unit)
}