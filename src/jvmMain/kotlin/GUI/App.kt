package GUI

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.FrameWindowScope
import model.*
import storage.*
import ui.*

/**
 * The principal Composable function of the application.
 * It is responsible for the creation of the application's UI.
 * The content of the application window.
 * @param onExit the function to be called when the application is closed.
 * @param storage the storage to be used by the application.
 */
@Composable
fun FrameWindowScope.ReversiApp(onExit: ()->Unit, storage: BoardStorage) {
    val scope = rememberCoroutineScope()
    val vm = remember { ReversiViewModel(scope, storage) }       // The ViewModel.
    ReversiMenu(vm, onExit)                        // The App menu.
    ReversiDialog(vm)                              // The Dialogs.
    // Content of the application window.
    Column {
        if(!tes)BoardView(vm.game?.board, onClick = vm::play)
        else {
            var t: Board? = null
            when(val b= g?.board) {
                is BoardRun -> {
                    t= b
                    t.turn= g?.player ?: t.turn
                }
                is BoardWin -> StatusInfo("Winner",b.winner)
                is BoardDraw -> StatusInfo("Draw")
                null -> StatusInfo("No Game")
            }
            BoardView(t,onClick = vm::play)
        }
        StatusBar(vm.status)
    }
}