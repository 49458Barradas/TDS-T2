package GUI

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import model.*

// Dimensions of the board presentation.
val cellSize = 150.dp
val lineSize = 10.dp
val boardSize = cellSize * BOARD_DIM + lineSize*(BOARD_DIM -1)

//player x = WHITE
@Composable
@Preview
fun BoardViewTest() =
    BoardView( createBoard(Player.WHITE).play("3E".toCell()).play("5F".toCell())) {}

/**
 * The Composable function responsible for the presentation of the board.
 * @param board the board to be presented.
 * @param onClick the function to be called when a cell is clicked.
 */
@Composable
fun BoardView(board: Board?, onClick: (Cell) -> Unit) {
    if (board == null) {
        Box(modifier = Modifier.size(boardSize).background(Color.LightGray))
    } else {
        Column(
            modifier = Modifier.size(boardSize).background(Color.Black),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Row for the column headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("") // This empty text is to make space for the row numbers on the left
                //val columns1 = listOf("A", "B", "C", "D) // Adjust for your game's column letters
                Column.values.forEach { colLetter ->
                    Text(
                        text = colLetter.toString(),
                        modifier = Modifier.size(lineSize),
                        color = Color.White,
                        textAlign = TextAlign.Center //not working properly
                    )
                }
            }
            // Now add the rest of the rows for the board
            repeat(BOARD_DIM) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${row+1}",
                        modifier = Modifier.size(lineSize),
                        textAlign = TextAlign.Center,
                        color = Color.White // adjust text color according to your requirements
                    )
                    repeat(BOARD_DIM) { col->
                        val pos = Cell(row, col)
                        CellView(board.moves[pos]) { onClick(pos) }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun CelViewTest() = CellView(Player.WHITE) {}

/**
 * The Composable function responsible for the presentation of each cell.
 * @param player the player that played on the cell.
 * @param modifier the modifier to be applied to the cell.
 * @param onClick the function to be called when the cell is clicked.
 */
@Composable
fun CellView(
    player: Player?,
    modifier: Modifier = Modifier.size(cellSize).background(Color.Green),
    onClick: () -> Unit = {},
) {
    if (player==null)
        Box(modifier = modifier.clickable(onClick = onClick))
    else
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ){
            var fill by remember(player) { mutableStateOf(0.1f) }
            // Initialize a Sprites object with a .png file
            val sprites = Sprites("sprites1.png", IntSize(64,64))

            // Get a subimage at a specific offset and size
            val subImage1 = sprites.get(1,6)

            // Get a subimage at a specific row and column assuming default size of (64,64)
            val subImage2 = sprites.get(0, 6)
            val image = when (player) {
                Player.BLACK -> subImage1
                Player.WHITE -> subImage2
            }
            Image(bitmap = image, modifier = Modifier.fillMaxSize(), contentDescription = "Image of player ${player.name}")
            LaunchedEffect(player) {
                while(fill<1f) {
                    delay(50)
                    fill *= 2
                }
                fill = 1f
            }
        }
}