package org.ncgroup.versereach.wear

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import org.ncgroup.versereach.wear.navigation.RootNavigation
import org.ncgroup.versereach.wear.theme.AppTheme


@Composable
internal fun App(
    modifier: Modifier = Modifier
) = AppTheme {
    PreComposeApp {
        val navigator = rememberNavigator()
        Scaffold(
            modifier = modifier.background(MaterialTheme.colors.onSurfaceVariant)
        ) {
            RootNavigation(navigator = navigator)
        }
    }
}