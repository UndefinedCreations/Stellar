package com.undefined.stellar.data.requirement

/**
 * An object class with properties each representing a [Minecraft Permission Level](https://minecraft.wiki/w/Permission_level).
 */
object PermissionLevel {
    /**
     * Permission level 0, no permission.
     */
    const val DEFAULT = 0
    /**
     * Permission level 1, can bypass spawn protection.
     */
    const val MODERATOR = 1
    /**
     * Permission level 2, more commands, can use command blocks, and more.
     */
    const val GAME_MASTER = 2
    /**
     * Permission level 3, commands related to multiplayer management are available.
     */
    const val ADMIN = 3
    /**
     * Permission level 4, all commands are including commands related to server management.
     */
    const val OWNER = 4
}