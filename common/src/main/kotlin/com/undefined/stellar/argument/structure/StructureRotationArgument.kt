package com.undefined.stellar.argument.structure

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.block.structure.StructureRotation

/**
 * An argument that allows you to pass in a one of the following: `none`, `clockwise_90`, `counterclockwise_90` and `180`. returning [StructureRotation].
 *
 * @since 1.19
 */
class StructureRotationArgument(name: String) : AbstractStellarArgument<StructureRotationArgument, StructureRotation>(name)