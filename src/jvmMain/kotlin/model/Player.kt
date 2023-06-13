package model

/**
 * Represents a player in the game.
 */
enum class Player(val symbol: Char) {
    BLACK('#'), WHITE('@');

    fun turn() = if(this == BLACK) WHITE else BLACK
}