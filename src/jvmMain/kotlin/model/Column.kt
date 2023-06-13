package model


/**
 * Represents a column in the board.
 * The column index is the character value minus 'A'.
 * @param symbol the Column symbol, in the range ['A'..'A' + BOARD_DIM]
 * @property index the Column index, in the range [0..BOARD_DIM-1]
 * @property values the list of all valid columns
 * @constructor If within possible range, returns the column with the given symbol, otherwise throws an exception
 * @throws IllegalArgumentException if the symbol is not a valid column
 */
data class Column private constructor(val symbol: Char){

    init{
        require(symbol in 'A'..'A' + BOARD_DIM){"Invalid column $symbol"}
    }

    val index = symbol - 'A'

    override fun toString(): String = "Column $symbol"

    companion object {
        val values = (1..BOARD_DIM).map { Column('A'+ it - 1) }

        operator fun invoke(char: Char) = values[char-'A']
    }

}


/**
 * Converts a character to a column, or null if the character is not a valid column.
 */
fun Char.toColumnOrNull() = if ('A' <= this && this < 'A' + BOARD_DIM) Column(this) else null

/**
 * Converts a character to a column, or throws an exception if the character is not a valid column.
 */
fun Char.toColumn() = toColumnOrNull() ?: throw IllegalArgumentException("Invalid column $this")
