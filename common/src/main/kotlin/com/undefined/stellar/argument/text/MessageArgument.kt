package com.undefined.stellar.argument.text

import com.undefined.stellar.ParameterArgument
import net.kyori.adventure.text.Component

/**
 * An argument that allows you to pass in a phrase, replacing any target selectors (`@s`, `@e`, etc.) with their actual result, returning [Component]
 * @since 1.13
 */
class MessageArgument(name: String) : ParameterArgument<MessageArgument, Component>(name)