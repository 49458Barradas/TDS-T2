package model
import storage.Serializer

object BoardSerializer : Serializer<Board,String> {
    override fun serialize(data: Board) = buildString {
        appendLine( when(data) {
            is BoardRun -> "Run:${data.turn}"
            is BoardWin -> "Win:${data.winner}"
            is BoardDraw -> "Draw:-"
        })
        appendLine(data.moves.entries.joinToString(" ") {
            "${it.key}:${it.value.name}"
        })
    }
    override fun deserialize(stream: String): Board {
        val (header, movesLine) = stream.split("\n")
        val (kind,player) = header.split(":")
        val moves =
            if (movesLine.isEmpty()) emptyMap()
            else movesLine.split(" ").associate {
                val (k,v) = it.split(":")
                k.toCell() to Player.valueOf(v)
            }
        return when(kind) {
            "Run" -> BoardRun(moves, Player.valueOf(player))
            "Win" -> BoardWin(moves, Player.valueOf(player))
            "Draw" -> BoardDraw(moves)
            else -> error("Invalid board kind: $kind")
        }
    }
}
