package org.ncgroup.mymail


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import org.ncgroup.mymail.navigation.BottomNavigation
import org.ncgroup.mymail.navigation.RootNavigation


@Composable
internal fun App(
    modifier: Modifier = Modifier
) = MaterialTheme {

    PreComposeApp {

        val navigator = rememberNavigator()

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    navigator = navigator
                )
            }
        ){ paddingValues ->
            Box(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ){
                RootNavigation(
                    navigator = navigator
                )
            }
        }

    }

}

internal expect fun openUrl(url: String?)