import androidx.compose.ui.window.ComposeUIViewController
import org.ncgroup.mymail.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
