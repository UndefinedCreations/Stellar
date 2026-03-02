package com.undefined.stellar.context

interface CommandContext<S : Any> {
//    fun sender(): S
    val sender: S
}