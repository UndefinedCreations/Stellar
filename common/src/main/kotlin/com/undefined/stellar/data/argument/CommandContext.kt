package com.undefined.stellar.data.argument

import com.undefined.stellar.exception.ArgumentCastMismatchException
import org.bukkit.command.CommandSender

typealias CommandNode = HashMap<String, (CommandContext<CommandSender>) -> Any?>

@Suppress("UNCHECKED_CAST", "UNUSED")
class CommandContext<T : CommandSender>(val arguments: CommandNode, val sender: T, val input: String) {

    inline fun <reified T> getArgument(name: String): T {
        println("name: $name")
        println("arguments: ${arguments.map { it.key }}")
        val argument = arguments[name] ?: throw NoSuchElementException("No argument with the name $name")
        val context = this as CommandContext<CommandSender>
        println("argument output: ${argument(context)}")
        println("context input: ${context.input}")
        println("argument class: ${argument(context)?.let {  it::class.simpleName } ?: "null"}")
        return argument(context) as? T
            ?: throw ArgumentCastMismatchException("Argument of name '$name' cannot be cast into ${T::class.simpleName}! Check if the argument you are getting returns this class.")
    }

    inline fun <reified T> getArgument(index: Int): T =
        arguments.values.toList()[index](this as CommandContext<CommandSender>) as? T
            ?: throw ArgumentCastMismatchException("Argument of index '$index' cannot be cast into ${T::class.simpleName}!  Check if the argument you are getting returns this class.")

    operator fun get(index: Int) = arguments.values.toList()[index](this as CommandContext<CommandSender>)
    operator fun get(name: String) = (arguments[name]
        ?: throw NoSuchElementException("No argument with the name $name"))(this as CommandContext<CommandSender>)

}