package com.undefined.stellar.data.argument

import com.undefined.stellar.data.exception.ArgumentCastMismatchException
import com.undefined.stellar.nms.NMSHelper
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
class CommandContext<T : CommandSender>(
    private val brigadierContext: com.mojang.brigadier.context.CommandContext<*>,
    val args: CommandTree,
    val sender: T,
    val input: String,
) {

    /**
     * A delegate operator function that gets the argument with the name of the property, in the type as specified by [T].
     *
     * @return The argument with the type of [T].
     * @throws NoSuchElementException If the argument cannot be found.
     * @throws ArgumentCastMismatchException If the argument cannot be cast into [T].
     */
    inline operator fun <reified T> CommandTree.getValue(nothing: Nothing?, property: KProperty<*>): T =
        (args[property.name] ?: throw NoSuchElementException("No argument with the name ${property.name}"))(this@CommandContext as CommandContext<CommandSender>) as T

    /**
     * Gets the argument with [name], in the type as specified by [T].
     *
     * @return The argument with the type of [T].
     * @throws NoSuchElementException If the argument cannot be found.
     * @throws ClassCastException If the argument cannot be cast into [T].
     */
    fun <T> getArgument(name: String): T {
        val argument = args[name] ?: throw NoSuchElementException("No argument with the name $name")
        val context = this as CommandContext<CommandSender>
        return argument(context) as T
    }

    /**
     * Gets the argument at [index], in the type as specified by [T].
     *
     * @return The argument with the type of [T].
     * @throws NoSuchElementException If the argument cannot be found.
     * @throws ClassCastException If the argument cannot be cast into [T].
     */
    fun <T> getArgument(index: Int): T = args.values.toList()[index](this as CommandContext<CommandSender>) as T

    /**
     * Gets the argument with [name], in the type as specified by [T].
     *
     * @return The argument with the type of [T], which returns null if the argument cannot be cast successfully or the argument cannot be found.
     * @throws NoSuchElementException If the argument cannot be found.
     */
    fun <T> getOrNull(name: String): T? {
        val argument = args[name] ?: return null
        val context = this as CommandContext<CommandSender>
        return argument(context) as? T
    }

    /**
     * Gets the argument at [index], in the type as specified by [T].
     *
     * @return The argument with the type of [T], which returns null if the argument cannot be cast successfully or the argument cannot be found.
     * @throws NoSuchElementException If the argument cannot be found.
     */
    fun <T> getOrNull(index: Int): T? =
        args.values.toList().getOrNull(index)?.invoke(this as CommandContext<CommandSender>) as? T

    /**
     * Gets the argument with [name].
     *
     * @return The argument as an [Object] or a [Any] (in Kotlin), which returns null if the argument cannot be cast successfully.
     * @throws NoSuchElementException If the argument cannot be found.
     */
    operator fun get(name: String): Any? = (args[name]
        ?: throw NoSuchElementException("No argument with the name $name"))(this as CommandContext<CommandSender>)

    /**
     * Gets the argument at [index].
     *
     * @return The argument as an [Object] or a [Any] (in Kotlin), which returns null if the argument cannot be cast successfully.
     * @throws NoSuchElementException If the argument cannot be found.
     */
    operator fun get(index: Int): Any? = args.values.toList()[index](this as CommandContext<CommandSender>)

    /**
     * Gets the input of a certain argument, if no argument is found, it will throw an [IllegalArgumentException].
     *
     * @param name A [String] representing the name of an argument.
     * @return The raw input in [String] of the argument with [name].
     * @throws IllegalArgumentException If no argument input is found with [name].
     */
    fun getArgumentInput(name: String): String = NMSHelper.getArgumentInput(brigadierContext, name)
        ?: throw IllegalArgumentException("No argument input found with name $name.")

    /**
     * Gets the input of a certain argument, if no argument is found, it will return `null`.
     *
     * @param name A [String] representing the name of an argument.
     * @return The raw input in a nullable [String] of the argument with [name].
     */
    fun getArgumentInputOrNull(name: String): String? = NMSHelper.getArgumentInput(brigadierContext, name)

}