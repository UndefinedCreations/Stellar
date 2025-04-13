package com.undefined.stellar.data.argument

import com.undefined.stellar.data.exception.ArgumentCastMismatchException
import org.bukkit.command.CommandSender
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST", "UNUSED")
/**
 * The `CommandContext` class contains information to be used in method calls in run-time. It is most often used in executions, suggestions and requirements.
 *
 * @param sender This property has the type of whatever you are specified in the `CommandContext<T>` generic type.
 * This often comes from the generic type of the method it is being used on, such as `addExecution<T>` and `addRequirement<T>`.
 * This must always be a subclass of [CommandSender], `sender` is the sender of the command cast into whatever you want it to be.
 *
 * @param args A [HashMap] of arguments of with the key being the argument name, and the value being its return value.
 * You usually don't interact with this property directly, instead using one of the methods provided. You can use `getArgument<Return Value Class>(index or name)` or `get(index or name)/context[index or name]`.
 *
 * @param input A [String] that represents the entire command input excluding the `/`.
 */
class CommandContext<T : CommandSender>(val args: CommandNode, val sender: T, val input: String) {

    /**
     * A delegate operator function that gets the argument with the name of the property, in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @throws ArgumentCastMismatchException If the argument cannot be cast into [T].
     * @return The argument with the type of [T].
     */
    inline operator fun <reified T> CommandNode.getValue(nothing: Nothing?, property: KProperty<*>): T =
        (args[property.name] ?: throw NoSuchElementException("No argument with the name ${property.name}"))(this@CommandContext as CommandContext<CommandSender>) as T

    /**
     * Gets the argument with [name], in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @throws ArgumentCastMismatchException If the argument cannot be cast into [T].
     * @return The argument with the type of [T].
     */
    inline fun <reified T> getArgument(name: String): T {
        val argument = args[name] ?: throw NoSuchElementException("No argument with the name $name")
        val context = this as CommandContext<CommandSender>
        return argument(context) as? T
            ?: throw ArgumentCastMismatchException("Argument of name '$name' cannot be cast into ${T::class.simpleName}! Check if the argument you are getting returns this class.")
    }

    /**
     * Gets the argument at [index], in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @throws ArgumentCastMismatchException If the argument cannot be cast into [T].
     * @return The argument with the type of [T].
     */
    inline fun <reified T> getArgument(index: Int): T =
        args.values.toList()[index](this as CommandContext<CommandSender>) as? T
            ?: throw ArgumentCastMismatchException("Argument of index '$index' cannot be cast into ${T::class.simpleName}!  Check if the argument you are getting returns this class.")

    /**
     * Gets the argument with [name], in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @return The argument with the type of [T], which returns null if the argument cannot be cast successfully.
     */
    inline fun <reified T> getOrNull(name: String): T? {
        val argument = args[name] ?: throw NoSuchElementException("No argument with the name $name")
        val context = this as CommandContext<CommandSender>
        return argument(context) as? T
    }

    /**
     * Gets the argument at [index], in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @return The argument with the type of [T], which returns null if the argument cannot be cast successfully.
     */
    inline fun <reified T> getOrNull(index: Int): T? =
        args.values.toList()[index](this as CommandContext<CommandSender>) as? T

    /**
     * Gets the argument with [name].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @return The argument as an [Object] or a [Any] (in Kotlin), which returns null if the argument cannot be cast successfully.
     */
    operator fun get(name: String): Any? = (args[name]
        ?: throw NoSuchElementException("No argument with the name $name"))(this as CommandContext<CommandSender>)

    /**
     * Gets the argument at [index].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @return The argument as an [Object] or a [Any] (in Kotlin), which returns null if the argument cannot be cast successfully.
     */
    operator fun get(index: Int): Any? = args.values.toList()[index](this as CommandContext<CommandSender>)

}