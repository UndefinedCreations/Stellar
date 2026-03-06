package com.undefined.stellar.papermc.command

import com.undefined.stellar.command.Command

interface PaperCommand : Command {
    companion object {
        fun create(name: String): PaperCommand = PaperCommandImpl(name)
    }
}