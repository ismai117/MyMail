package org.ncgroup.versereach

import androidx.compose.ui.window.ComposeUIViewController

actual fun getPlatformName(): String {
    return ""
}

fun MainViewController() = ComposeUIViewController { App() }

actual fun openUrl(url: String?) {
}