package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import net.kyori.adventure.text.Component

class MessageArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<MessageArgument, Component>(parent, name)