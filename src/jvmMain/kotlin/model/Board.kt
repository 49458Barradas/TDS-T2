package model
import ui.Game
import ui.pass

const val BOARD_DIM = 4

typealias Moves = Map<Cell, Player>

/**
 * Represents the pieces in the board
 * @param moves the map of all pieces in the board
 */
sealed class Board(val moves: Moves) {
    override fun equals(other: Any?): Boolean {
        if(this===other) return true
        if(other !is Board) return false
        return if(this::class != other::class) false
        else false
    }
    override fun hashCode():Int = moves.hashCode()
}

/**
 * Represents the board in the middle of a game.
 */
class BoardRun(moves: Moves, var turn: Player): Board(moves)

/**
 * Represents the board in the end of a game, when a player wins.
 */
class BoardWin(moves: Moves, val winner: Player): Board(moves)

/**
 * Represents the board in the end of a game, when there is a draw.
 */
class BoardDraw(moves: Moves) : Board(moves)

/**
 * Creates the initial board.
 */
fun initialBoard(): Moves{
    val boardMap = mutableMapOf<Cell, Player>()
    boardMap+=Pair(Cell(Row(2),Column('B')), Player.WHITE)
    boardMap+=Pair(Cell(Row(2),Column('C')), Player.BLACK)
    boardMap+=Pair(Cell(Row(3),Column('B')), Player.BLACK)
    boardMap+=Pair(Cell(Row(3),Column('C')), Player.WHITE)
    return boardMap
}

fun Board.get(c: Cell)= moves.get(c)

fun createBoard(first: Player) = BoardRun(initialBoard(), first)

/**
 * Returns the legal moves for the current player in the current board state.
 * @return the list of legal moves
 */
fun BoardRun.getLegalMoves(): List<Cell> {
    val legalMoves = mutableListOf<Cell>()
    moves.forEach { (cell, player) ->
        if (player == this.turn) {
            Direction.values().forEach { direction ->
                val temp = cell + direction
                if (temp != Cell.INVALID) {
                    if (player != moves.get(temp)) {
                        val tempList = cellsInDirection(cell, direction)
                        var i = 0
                        while(moves[tempList[i]]==turn.turn()&&i<tempList.size-1){
                            i++
                        }
                        val cell = tempList[i]-direction
                        val teste = moves.get(tempList[i]-direction)
                        if(moves.get(tempList[i])==null&&moves.get(tempList[i]-direction)==turn.turn()){
                            legalMoves.add(tempList[i])
                        }
                    }
                }
            }
        }
    }
    return legalMoves
}

/**
 * Makes the given play and alters game state accordingly.
 * @param pos the position to play
 */
fun Board.play(pos: Cell):Board = when(this){
    is BoardRun -> {
        var illegal = false
        val mutableMap = mutableMapOf<Cell, Player>().apply { putAll(moves) }
        val legalMoves = getLegalMoves()
        if (pos in legalMoves) {
            pass = 0
            Direction.values().forEach { direction ->
                val cellsInDir = cellsInDirection(pos, direction)
                var i = 0
                if(cellsInDir.size!=0){
                    if (moves.get(cellsInDir[0]) == turn.turn() && i<cellsInDir.size-1) {
                        i++
                        while (moves.get(cellsInDir[i]) == turn.turn() && i<cellsInDir.size-1) {
                            i++
                        }
                        if (moves.get(cellsInDir[i]) == turn) {
                            for (p in 0 until i) {
                                mutableMap[cellsInDir[p]] = turn
                            }
                        }
                    }
                }
            }
            mutableMap[pos] = turn
        }
        else{
            BoardRun(mutableMap, turn)
            illegal = true
        }
        val tempCheck = isWin()
        val tempCheckDraw = isDraw()
        when {
            tempCheckDraw -> BoardDraw(mutableMap)
            tempCheck.first -> BoardWin(mutableMap, tempCheck.second!!)
            moves.size == BOARD_DIM * BOARD_DIM && moves.count { it.value == turn } == moves.count { it.value == turn.turn() } -> BoardDraw(
                moves)
            illegal -> BoardRun(mutableMap, turn)
            else -> BoardRun(mutableMap, turn.turn())
        }
    }
    is BoardWin, is BoardDraw -> error("Game Over")
}

fun Board.isNew(): Boolean{
    if(this is BoardRun){
        return moves.size==4 && pass==0
    }
    return false
}

/**
 * Verifies if the game is a draw.
 * @return true if the game is a draw, false otherwise
 */
fun BoardRun.isDraw(): Boolean{
    val bool= moves.size == BOARD_DIM * BOARD_DIM || pass == 2
    if(moves.count{it.value==turn}==moves.count{it.value==turn.turn()} && bool)return true
    else return false
}

/**
 * Verifies if the game is over and returns the winner.
 * @return true if the game is over, false otherwise and the winner is put as the second element of the pair
 */
fun BoardRun.isWin(): Pair<Boolean,Player?> {
    val bool = moves.size == BOARD_DIM * BOARD_DIM - 1 || pass == 2
    val winner: Player? = if(moves.count{it.value==turn}>moves.count{it.value==turn.turn()}) turn
    else {
        if(moves.count{it.value==turn}<moves.count{it.value==turn.turn()}) turn.turn()
        else null
    }
    return Pair(bool, winner)
}

/**
 * Prints the board.
 * @param game the game to print
 * @param local true if the game is local, false otherwise
 * @param targets true if the legal moves should be printed, false otherwise
 */
fun printBoard(game: Game, local:Boolean, targets: Boolean){
    val l= mutableListOf<Cell>()
    if(targets && game.board is BoardRun)l.addAll(game.board.getLegalMoves())
    if(local && (game.board.moves.size==4 && pass==0))println("This is a local Game, Player ${game.player.symbol} starts")
    else if(!local) println("You are Player ${game.player.symbol} in game ${game.id}")
    var col=0
    print("  ")
    while(col!= BOARD_DIM){
        print('A'+col + " ")
        col++
    }
    col = 0
    var line= 2
    while(line<= BOARD_DIM){
        println()
        print("${line-1} ")
        Cell.values.forEach { cell ->
            val CellVal = game.board.get(cell)
            if(cell in l){
                print("* ")
            }
            else if(CellVal == null ) print(". ")
            else print("${CellVal.symbol} ")
            ++col
            if(col== BOARD_DIM){
                col=0
                ++line
                if(line<10)print("\n${line-1} ")
            }
        }
        println()
    }
    println( when(game.board) {
        is BoardRun -> if(local)"turn: ${game.board.turn.symbol}" else "turn: ${game.board.turn.symbol}"
        is BoardWin -> "winner: ${game.board.winner}"
        is BoardDraw -> "Draw"
    } )
}
