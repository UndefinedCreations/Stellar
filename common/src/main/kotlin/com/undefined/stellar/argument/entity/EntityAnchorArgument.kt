package com.undefined.stellar.argument.entity

import com.undefined.stellar.ParameterArgument
import com.undefined.stellar.data.argument.EntityAnchor

/**
 * An argument that allows you to type either `eyes` or `feet`, and returns an instance of [EntityAnchor].
 */
class EntityAnchorArgument(name: String) : ParameterArgument<EntityAnchorArgument, EntityAnchor>(name)