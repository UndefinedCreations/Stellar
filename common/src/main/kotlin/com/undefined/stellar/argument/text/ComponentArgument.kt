package com.undefined.stellar.argument.text

import com.undefined.stellar.ParameterArgument
import net.kyori.adventure.text.Component

/**
 * An argument that allows you to pass in a valid JSON text component, returns [Component].
 * @since 1.13
 */
class ComponentArgument(name: String) : ParameterArgument<ComponentArgument, Component>(name)