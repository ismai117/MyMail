package org.ncgroup.versereach


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import org.ncgroup.versereach.navigation.RootNavigation
import org.ncgroup.versereach.theme.AppTheme


@Composable
internal fun App(
    modifier: Modifier = Modifier
) = AppTheme {

    PreComposeApp {

        val navigator = rememberNavigator()

        Scaffold(
            contentWindowInsets = WindowInsets(0.dp)
        ){
            RootNavigation(
                navigator = navigator
            )
        }

    }

}


expect fun getPlatformName(): String
expect fun openUrl(url: String?)