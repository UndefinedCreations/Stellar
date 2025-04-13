package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarArgument
import net.kyori.adventure.text.Component

/**
 * An argument that allows you to pass in a valid JSON text component, returns [Component].
 */
class ComponentArgument(name: String) : AbstractStellarArgument<ComponentArgument, Component>(name)