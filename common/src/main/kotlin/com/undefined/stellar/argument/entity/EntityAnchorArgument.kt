package com.undefined.stellar.argument.entity

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.argument.EntityAnchor

class EntityAnchorArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<EntityAnchorArgument, EntityAnchor>(parent, name)