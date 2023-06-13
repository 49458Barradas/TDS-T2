package model

/**
 * Represents a cell in the board
 * @param idx the index of the cell in the board
 * @property row the row of the cell, or null if the index is invalid
 * @property col the column of the cell, or null if the index is invalid
 * @property rowIndex the row index of the cell, in the range [0..BOARD_DIM-1]
 * @property colIndex the column index of the cell, in the range [0..BOARD_DIM-1]
 * @property INVALID the invalid cell
 * @property values the list of all valid cells
 * @constructor If within possible range, returns the cell with the given index, otherwise returns INVALID
 */
data class Cell(val idx: Int){

    val row = ifPossibleRow(idx)
    val col = ifPossibleColumn(idx)

    operator fun minus(other: Direction): Cell{
        return when{
            other==Direction.UP -> this + Direction.DOWN
            other==Direction.DOWN -> this + Direction.UP
            other==Direction.UP_RIGHT -> this + Direction.DOWN_LEFT
            other==Direction.UP_LEFT -> this + Direction.DOWN_RIGHT
            other==Direction.LEFT -> this + Direction.RIGHT
            other==Direction.DOWN_LEFT -> this + Direction.UP_RIGHT
            other==Direction.DOWN_RIGHT -> this + Direction.UP_LEFT
            else -> this + Direction.LEFT
        }
    }

    operator fun plus(other: Direction): Cell {
        val temp = idx + other.difRow * BOARD_DIM + other.difCol
        when{
            temp !in 0 until BOARD_DIM * BOARD_DIM -> return INVALID
            this.colIndex == BOARD_DIM-1 && other.difCol==1 -> return INVALID
            this.colIndex == 0 && other.difCol==-1 -> return INVALID
            else -> return Cell(temp)
        }
    }

    /**
     * Returns the column of the cell if the index is valid, null otherwise
     * @param idx the index of the cell
     */
    private fun ifPossibleColumn(idx: Int): Column? {
        return if(idx in 0 until BOARD_DIM * BOARD_DIM) Column('A' + idx % BOARD_DIM)
        else null
    }

    /**
     * Returns the row of the cell if the index is valid, null otherwise
     * @param idx the index of the cell
     */
    private fun ifPossibleRow(idx: Int): Row? {
        return if(idx in 0 until BOARD_DIM * BOARD_DIM) Row(idx / BOARD_DIM + 1)
        else null
    }

    val rowIndex = idx / BOARD_DIM
    val colIndex = idx % BOARD_DIM

    override fun toString(): String = if(this.row != null && this.col != null) "${row.number}${col.symbol}" else "Invalid Cell"

    companion object{

        val INVALID = Cell(-1)

        val values = (0 until BOARD_DIM * BOARD_DIM).map { Cell(it) }

        operator fun invoke(row: Row, col: Column): Cell {
            return if(row.index * BOARD_DIM + col.index in 0 until BOARD_DIM * BOARD_DIM) values[row.index * BOARD_DIM + col.index]
            else INVALID
        }

        operator fun invoke(row: Int, col: Int): Cell {
            return if(row in 0 until BOARD_DIM && col in 0 until BOARD_DIM) values[row * BOARD_DIM + col]
            else INVALID
        }
    }
}

/**
 * Converts a string to a cell, or null if the string is not a valid cell.
 */
fun String.toCellOrNull(): Cell? {
    if(this.length!=2 || this[0].digitToInt().toRowOrNull()==null || this[1].toColumnOrNull()==null) return null
    else return Cell(this[0].digitToInt().toRow(), this[1].toColumn())
}

/**
 * Converts a string to a cell, or throws an exception if the string is not a valid cell.
 */
fun String.toCell(): Cell {
    if(this.length!=2)
        throw IllegalArgumentException("Stuff.Cell must have row and column")
    if(this[0].digitToInt().toRowOrNull()==null)
        throw IllegalArgumentException("Invalid row ${this[0]}")
    if(this[1].toColumnOrNull()==null)
        throw IllegalArgumentException("Invalid column ${this[1]}")
    else return Cell(this[0].digitToInt().toRow(), this[1].toColumn())
}

/**
 * Represents a column in the board.
 * @param symbol the column symbol, in the range ['A'..'H']
 * @param direction the direction in which we want to count the cells
 * @property index the column index, in the range [0..BOARD_DIM-1]
 */
fun cellsInDirection(cell : Cell, direction: Direction): List<Cell>{
    val answerList = mutableListOf<Cell>()
    var i = 0
    var nowCell = cell
    while(nowCell+direction != Cell.INVALID){
        nowCell += direction
        answerList.add(i, nowCell)
        i++
    }
    return answerList
}