package ui
import model.*
import storage.Storage
import GUI.*
var pass = 0
var targets = false

/**
 * Represents each side of the game.
 */
typealias BoardStorage = Storage<String, Board>

data class Game(
    val board: Board,
    val player: Player,
    val id: String
)

/**
 * Creates a new game.
 */

fun createGame(local:Boolean, id: String, st: BoardStorage) =
    if(local){Game(createBoard(Player.WHITE), Player.WHITE, id).also {
        st.create(it.id, it.board)
    }}
    else{Game(createBoard(Player.BLACK), Player.BLACK, id).also {
        st.create(it.id, it.board)
    }}

/**
 * Joins an existing game.
 */
suspend fun joinGame(id: String, st: BoardStorage): Game {
    val board = checkNotNull( st.read(id) ) { "Game not found" }
    check( board is BoardRun && board.moves.size <= 5 ) { "Game is not available" }
    return Game(board, board.turn, id)//possivelment alterar para ifs
}
fun Game.pass(st:BoardStorage): Game{
    require(board is BoardRun){"Game Over"}
    if(!isLocal)check(player == board.turn ) { "Is not your turn" }
    val tempCheck: Pair<Boolean, Player?>
    if(pass==2){
        tempCheck= board.isWin()
        if(tempCheck.first){
            if(tempCheck.second==null){
                return Game(BoardDraw(board.moves), board.turn, id)
            }
            else return(Game(BoardWin(board.moves, tempCheck.second!!),board.turn.turn(),id))
        }
    }
    return(Game(board,board.turn.turn(),id))
}

/**
 * Makes a move in the game.
 */
fun Game.play(pos: Cell, st: BoardStorage,local:Boolean): Game {
    if (board is BoardRun){
        if(!isLocal)check(player == board.turn ) { "Is not your turn" }
    }
    tes= false
    pass=0
    return copy( board= board.play(pos) ).also { st.update(id, it.board) }
}

/**
 * Refreshes the game.
 */
suspend fun Game.refresh(st: BoardStorage): Game {
    val board = st.read(id) ?: throw Exception("Game not found")
    check( board != this.board ) { "No changes" }
    return copy( board = board )
}

/**
 * Shows the game.
 */
suspend fun Game.show(st: BoardStorage): Game{
    val board = st.read(id) ?: throw Exception("Game not found")

    printBoard(copy( board = board ), isLocal, targets)
    return copy( board = board )
}

/**
 * Activates/Deactivates the target mode which shows the possible moves.
 */
fun targets(cmd: String, b: Boolean):Boolean {
    if(cmd=="on" && !b)return true
    if(cmd=="off" && b) return false
    return b
}