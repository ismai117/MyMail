package org.ncgroup.versereach

import androidx.compose.ui.window.ComposeUIViewController

actual fun getPlatformName(): String {
    TODO("Not yet implemented")
}

fun MainViewController() = ComposeUIViewController { App() }

actual fun openUrl(url: String?) {
}