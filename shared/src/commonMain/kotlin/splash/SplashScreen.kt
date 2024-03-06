package org.ncgroup.versereach.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import org.ncgroup.versereach.MainScreen
import org.ncgroup.versereach.email.EmailScreen


private typealias navigateToEmailScreen = () -> Unit

object SplashScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        SplashScreenContent(
            navigateToEmailScreen = {
                navigator.push(MainScreen)
            }
        )

    }

}


@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    navigateToEmailScreen: navigateToEmailScreen
){

    LaunchedEffect(Unit){
        delay(2000)
        navigateToEmailScreen()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "VerseReach",
            fontSize = 32.sp
        )

    }
}