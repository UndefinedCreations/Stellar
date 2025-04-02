package com.undefined.stellar.argument.text

import com.undefined.stellar.AbstractStellarArgument
import net.kyori.adventure.text.format.Style

/**
 * An argument that allows you to pass in a valid JSON text component, only returning its [Style].
 * @since 1.20.3
 */
class StyleArgument(name: String) : AbstractStellarArgument<StyleArgument, Style>(name)