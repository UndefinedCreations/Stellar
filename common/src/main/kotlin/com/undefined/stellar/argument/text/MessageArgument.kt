package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarArgument
import net.kyori.adventure.text.Component

/**
 * An argument that allows you to pass in a phrase, replacing any target selectors (`@s`, `@e`, etc.) with their actual result, returning [Component]
 */
class MessageArgument(name: String) : AbstractStellarArgument<MessageArgument, Component>(name)