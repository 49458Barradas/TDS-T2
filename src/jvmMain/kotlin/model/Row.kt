package model


/**
 * Represents a row in the board.
 * @param number the row number, in the range [1..BOARD_DIM]
 * @property index the row index, in the range [0..BOARD_DIM-1]
 * @property values the list of all valid rows
 * @constructor If within possible range, returns the row with the given number, otherwise throws an exception
 * @throws IllegalArgumentException if the number is not a valid row
 */
data class Row private constructor(val number: Int){

    init{
        require(number in 1..BOARD_DIM){"Invalid row $number"}
    }

    val index = number - 1

    override fun toString(): String = "Row $number"

    companion object {
        val values = (1..BOARD_DIM).map { Row(it) }

        operator fun invoke(number: Int) = values[number-1]
    }

}

/**
 * Converts an integer to a row, or null if the integer is not a valid row.
 */
fun Int.toRowOrNull() = if(this in 1..BOARD_DIM) Row(this) else null

/**
 * Converts an integer to a row, or throws an exception if the integer is not a valid row.
 */
fun Int.toRow() = toRowOrNull() ?: throw IllegalArgumentException("Invalid row $this")


