package com.undefined.stellar.papermc.command

import com.undefined.stellar.command.Command
import com.undefined.stellar.papermc.context.PaperCommandContext

interface PaperCommand : Command<PaperCommandContext> {
    companion object {
        fun create(name: String): PaperCommand = PaperCommandImpl(name)
    }
}