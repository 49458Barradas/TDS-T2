package GUI

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import model.*

/**
 * Menu of the application.
 */
@Composable
fun FrameWindowScope.ReversiMenu(vm: ReversiViewModel, onExit: ()->Unit, ) = MenuBar {
    Menu("Game") {
        Item("New", onClick = { vm.openDialog(Dialog.NEW) })
        Item("Join", onClick = { vm.openDialog(Dialog.JOIN) })
        Item("Refresh", enabled = vm.canRefresh, onClick = vm::refreshGame)
        Item("Exit", onClick = onExit)
    }
    Menu("Play") {
        Item("Pass", onClick = { vm.openDialog(Dialog.HELP) }) //TODO PASS
    }
    Menu("Options") {

        CheckboxItem("Auto Refresh",
            checked = vm.autoRefresh,
            onCheckedChange = { vm.toggleAutoRefresh() }
        )
        /*
        CheckboxItem("Show Targets",
            checked = vm.showTargets,
            onCheckedChange = { vm.toggleShowTargets() }
            )
         */
    }
}