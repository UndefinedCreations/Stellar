package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import net.kyori.adventure.text.format.Style

class StyleArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<StyleArgument, Style>(parent, name)