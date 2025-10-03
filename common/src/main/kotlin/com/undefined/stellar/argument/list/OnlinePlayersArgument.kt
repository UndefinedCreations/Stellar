package com.undefined.stellar.argument.list

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * An extension of [ListArgument] using [StringArgument] as a base. It lists the currently online players using `Bukkit.getServer().getOnlinePlayers`.
 *
 * @param name The name of the argument.
 * @param filter A function passing in a list of players and returning a filtered list in [CompletableFuture] (default: exclude sender).
 *
 * @since 1.13
 */
class OnlinePlayersArgument @JvmOverloads constructor(
    name: String,
    filter: CommandSender.(List<Player>) -> CompletableFuture<List<Player>> = { CompletableFuture.completedFuture(it.filter { it.uniqueId != (this as? Player)?.uniqueId }) },
) : ListArgument<Player, String>(
    base = StringArgument(name, StringType.WORD),
    list = { input ->
        filter(sender, Bukkit.getServer().onlinePlayers.toList()).thenApply { list ->
            list?.map { player -> Suggestion.withText(player.name) }
        }
    },
    parse = { input ->
        Bukkit.getPlayer(input)
    },
) {

    companion object {
        /**
         * Creates an [OnlinePlayersArgument].
         *
         * @param name The name of the argument.
         * @param filter A function passing in a list of players and returning a filtered list (default: exclude sender).
         * @return The created [OnlinePlayersArgument].
         */
        fun create(
            name: String,
            filter: CommandSender.(Player) -> Boolean = { it.uniqueId != (this as? Player)?.uniqueId },
        ): OnlinePlayersArgument = OnlinePlayersArgument(name) { CompletableFuture.completedFuture(it.filter { filter(it) }) }
    }

}