package GUI

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import model.*
import ui.*

/**
 * Composable responsible for the presentation of the dialogs.
 */


@Composable
fun ReversiDialog(vm: ReversiViewModel) =
    when (vm.open) {
        Dialog.NEW -> NameDialog(vm::closeDialog, vm::newGame, "New Game")
        Dialog.JOIN -> NameDialog(vm::closeDialog, vm::joinGame, "Join Game")
        Dialog.HELP -> GenericDialog(vm::closeDialog,"teste",vm::pass)
        Dialog.MESSAGE -> MessageDialog(vm::closeDialog, vm.message)
        null -> Unit
    }

/**
 * Generic component using AlertDialog that allows using the same code for all dialogs in the application.
 * These dialogs have a title, an Ok button, and an optional Cancel button.
 * @param onClose function called when the dialog is closed.
 * @param title the dialog title text.
 * @param onOk function called on Ok button (default is onClose).
 * @param onCancel function called on the Cancel button, or null (default) if the button is not displayed.
 * @param content the content of the dialog in the text area of the AlertDialog (default is empty).
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GenericDialog(
    onClose: ()->Unit,
    title: String,
    onOk: ()->Unit = onClose,
    onCancel: (()->Unit)? = null,
    content: @Composable () -> Unit = {}
) = AlertDialog(
    modifier = Modifier.width( boardSize * 0.8f ),
    onDismissRequest = onClose,
    title = { Text(title) },
    dismissButton = onCancel?.let { { Button(onClick = it){ Text("Cancel") } } },
    confirmButton = { Button(onClick = onOk){ Text("Ok") } },
    text = { content() },
)

/**
 * Dialog to ask for one name.
 * @param onClose function called when the dialog is dismissed.
 * @param onOk function called when the name is confirmed.
 * @param title the question about the name.
 */
@Composable
fun NameDialog(onClose: ()->Unit, onOk: (String, Boolean)->Unit, title: String) {
    var name by remember { mutableStateOf("") }
    var isLocal by remember { mutableStateOf(false) }

    GenericDialog(onClose, title,
        onOk = { onOk(name, isLocal) }, // call onOk with name and isLocal
        onCancel = onClose
    ) {
        Column {
            TextField(
                value = name,
                onValueChange = { name = it }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = isLocal,
                    onCheckedChange = { isLocal = it }
                )
                Text("Is this a local game?")
            }
        }
    }
}

/**
 * Dialog to show one message. Closed after 3 seconds or when Ok is clicked.
 * @param onClose function called when the dialog is dismissed.
 * @param message the message to be presented.
 */
@Composable
fun MessageDialog(onClose: ()->Unit, message: String) =
    GenericDialog(onClose, title = message) {
        LaunchedEffect(Unit) {
            delay(3000)
            onClose()
        }
    }

/**
 * Help dialog.
 * @param onClose the function to be called when the dialog is closed.
 */
@Composable
fun HelpDialog(onClose: ()->Unit) =
    GenericDialog(onClose, title = "Reversi") {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("ISEL", fontSize = 20.sp)
            Text("Técnicas de Desenvolvimento de Software")
        }
    }
