import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.ncgroup.versereach.MainView

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("VerseReach") { MainView() }
}