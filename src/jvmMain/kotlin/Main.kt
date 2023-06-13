package ttt

import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import model.*
import GUI.*
import storage.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.WindowState
import ui.BoardStorage

fun main() {
    MongoDriver().use { driver ->
        val storage: BoardStorage = MongoStorage("games", driver, BoardSerializer)
        //val storage: BoardStorage = TextFileStorage("games",BoardSerializer)
        application(exitProcessOnExit = false) {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Reversi TDS",
                state = WindowState(size = DpSize.Unspecified)
            ) {
                ReversiApp(::exitApplication,storage)
            }
        }
    }
}
