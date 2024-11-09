package com.undefined.stellar.data

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

class ColorData(private val color: TextColor, private val reset: Boolean) : Style  {
    override fun toBuilder(): Style.Builder {
        TODO("Not yet implemented")
    }

    override fun font(): Key? {
        TODO("Not yet implemented")
    }

    override fun font(font: Key?): Style {
        TODO("Not yet implemented")
    }

    override fun color(): TextColor? {
        TODO("Not yet implemented")
    }

    override fun color(color: TextColor?): Style {
        TODO("Not yet implemented")
    }

    override fun decoration(decoration: TextDecoration): TextDecoration.State {
        TODO("Not yet implemented")
    }

    override fun decoration(decoration: TextDecoration, state: TextDecoration.State): Style {
        TODO("Not yet implemented")
    }

    override fun decorations(decorations: MutableMap<TextDecoration, TextDecoration.State>): Style {
        TODO("Not yet implemented")
    }

    override fun clickEvent(): ClickEvent? {
        TODO("Not yet implemented")
    }

    override fun clickEvent(event: ClickEvent?): Style {
        TODO("Not yet implemented")
    }

    override fun hoverEvent(): HoverEvent<*>? {
        TODO("Not yet implemented")
    }

    override fun hoverEvent(source: HoverEventSource<*>?): Style {
        TODO("Not yet implemented")
    }

    override fun insertion(): String? {
        TODO("Not yet implemented")
    }

    override fun insertion(insertion: String?): Style {
        TODO("Not yet implemented")
    }

    override fun colorIfAbsent(color: TextColor?): Style {
        TODO("Not yet implemented")
    }

    override fun decorationIfAbsent(decoration: TextDecoration, state: TextDecoration.State): Style {
        TODO("Not yet implemented")
    }

    override fun merge(that: Style, strategy: Style.Merge.Strategy, merges: MutableSet<Style.Merge>): Style {
        TODO("Not yet implemented")
    }

    override fun unmerge(that: Style): Style {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }
}
