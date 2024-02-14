import androidx.compose.ui.window.ComposeUIViewController
import org.ncgroup.versereach.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
