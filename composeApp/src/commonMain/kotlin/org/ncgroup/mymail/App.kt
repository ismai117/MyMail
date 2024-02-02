package org.ncgroup.mymail


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import org.ncgroup.mymail.navigation.BottomNavigation
import org.ncgroup.mymail.navigation.RootNavigation
import org.ncgroup.mymail.theme.AppTheme


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

internal expect fun openUrl(url: String?)