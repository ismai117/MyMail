package org.ncgroup.versereach


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.ncgroup.versereach.splash.SplashScreen
import org.ncgroup.versereach.theme.AppTheme


@Composable
internal fun App(
    modifier: Modifier = Modifier
) = AppTheme {

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp)
    ){
        Navigator(SplashScreen){
            SlideTransition(it)
        }
    }
}


expect fun getPlatformName(): String
expect fun openUrl(url: String?)