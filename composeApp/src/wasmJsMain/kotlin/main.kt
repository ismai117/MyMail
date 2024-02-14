import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.ncgroup.versereach.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("VerseReach") { App() }
}