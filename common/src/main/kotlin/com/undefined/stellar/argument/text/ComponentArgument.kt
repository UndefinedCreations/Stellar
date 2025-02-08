package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import net.kyori.adventure.text.Component

class ComponentArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ComponentArgument, Component>(parent, name)