package com.undefined.stellar.data.phrase

import com.undefined.stellar.argument.phrase.PhraseArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.exception.ArgumentCastMismatchException
import org.bukkit.command.CommandSender

/**
 * The `PhraseCommandContext` class contains information to be used in method calls in run-time with [PhraseArgument]. It is most often used in executions, suggestions and requirements.
 *
 * @param globalContext The global [CommandContext] used outside the [PhraseArgument].
 *
 * @param arguments A list of words used in [PhraseArgument].
 *
 * @param sender This property has the type of whatever you are specified in the `CommandContext<T>` generic type.
 *  * This often comes from the generic type of the method it is being used on, such as `addExecution<T>` and `addRequirement<T>`.
 *  * This must always be a subclass of [CommandSender], `sender` is the sender of the command cast into whatever you want it to be.
 *
 * @param phraseInput A [String] that represents the phrase input.
 * @param input A [String] that represents the entire command input excluding the `/`.
 */
@Suppress("UNUSED")
class PhraseCommandContext<T : CommandSender>(val globalContext: CommandContext<T>, val arguments: PhraseCommandNode, val sender: T, val phraseInput: String, val input: String) {

    /**
     * Gets the word from the [index].
     *
     * @throws IndexOutOfBoundsException Whenever there is no word at the specified [index].
     */
    fun getArgument(index: Int): String = arguments[index]
    /**
     * Gets the word from the [index] or returns null if no argument is specified with that [index].
     */
    fun getArgumentOrNull(index: Int): String? = try { arguments[index] } catch (e: IndexOutOfBoundsException) { null }

    /**
     * Gets the argument with [name] from the global [CommandContext], in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @throws ArgumentCastMismatchException If the argument cannot be cast into [T].
     * @return The argument with the type of [T].
     */
    inline fun <reified T> getGlobalArgument(name: String): T = globalContext.getArgument(name)

    /**
     * Gets the argument at [index] from the global [CommandContext], in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @throws ArgumentCastMismatchException If the argument cannot be cast into [T].
     * @return The argument with the type of [T].
     */
    inline fun <reified T> getGlobalArgument(index: Int): T = globalContext.getArgument(index)

    /**
     * Gets the argument with [name] from the global [CommandContext], in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @return The argument with the type of [T], which returns null if the argument cannot be cast successfully.
     */
    inline fun <reified T> getGlobalArgumentOrNull(name: String): T? = globalContext.getOrNull(name)

    /**
     * Gets the argument at [index] from the global [CommandContext], in the type as specified by [T].
     *
     * @throws NoSuchElementException If the argument cannot be found.
     * @return The argument with the type of [T], which returns null if the argument cannot be cast successfully.
     */
    inline fun <reified T> getGlobalArgumentOrNull(index: Int): T? = globalContext.getOrNull(index)

    /**
     * Gets the word from the [index].
     *
     * @throws IndexOutOfBoundsException Whenever there is no word at the specified [index].
     */
    operator fun get(index: Int) = arguments[index]

}