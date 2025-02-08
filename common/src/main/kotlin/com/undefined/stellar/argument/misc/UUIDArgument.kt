package com.undefined.stellar.argument.misc

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import java.util.UUID

class UUIDArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<UUIDArgument, UUID>(parent, name)