package ui
import model.Board
import model.BoardRun
import model.getLegalMoves
import model.toCell
import storage.Storage
/*
data class Commands(
    val argsSyntax: String = "",
    val isToFinish: () -> Boolean = { false },
    val execute: (List<String>, Game) -> Game = { _, g -> g }
)

/**
 * Returns a map of all commands supported by the application.

fun getCommands(st: BoardStorage) = mapOf(
 */
    "PLAY" to Commands("<position>") { args, game ->
        require(args.isNotEmpty()) { "Missing position" }
        val pos = args[0].uppercase().toCell()
        checkNotNull(game) { "Invalid Move" }
        game.play(pos, st,local)
    },
    "NEW" to Commands("<game>"){ args, _ ->
        require(args.isNotEmpty()) { "Missing argument" }
        require(args[0]== "@" || args[0]== "#"){ "Wrong Argument please insert a valid piece to start"}
        if(args.size<2){
            local= true
            createGame(args[0],args[0],st)
        }
        else createGame(args[0],args[1], st)
    },
    "JOIN" to Commands("<game>"){ args, _ ->
        require(args.isNotEmpty()) { "Missing game" }
        joinGame(args[0], st)
    },
    "REFRESH" to Commands { _, game ->
        checkNotNull(game) { "Game not started" }
        game.refresh(st)
    },
    "PASS" to Commands { _, game ->
        val aux:Int
        if(game.board is BoardRun){
            aux= game.board.getLegalMoves().size
            require(aux==0){"Cannot pass"}
            if(aux==0) ++pass
        }
        game.pass(st)
    },
    "TARGETS" to Commands { args, game ->
        require(args.isNotEmpty()){"Missing argument"}
        require(args[0]== "on" || args[0]== "off"){"Invalid Input"}
        targets = targets(args[0], targets)
        game.show(st)
    },
    "SHOW" to Commands { _, game ->
        checkNotNull(game) { "Game not started" }//igual ao refresh????
        game.show(st)
    },
    "EXIT" to Commands(
        isToFinish = { true }
    )
)
 */