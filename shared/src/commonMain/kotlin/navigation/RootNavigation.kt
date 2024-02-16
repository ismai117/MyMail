package org.ncgroup.versereach.navigation


import androidx.compose.runtime.Composable
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import org.ncgroup.versereach.email.EmailScreen
import org.ncgroup.versereach.sms.SmsScreen
import org.ncgroup.versereach.splash.SplashScreen


private const val SPLASH = "splash_screen"
private const val EMAIL = "email_screen"
private const val SMS = "sms_screen"

@Composable
fun RootNavigation(
    navigator: Navigator
){
    NavHost(
        navigator = navigator,
        initialRoute = SPLASH,
        navTransition = NavTransition()
    ){
        scene(
            route = SPLASH,
            navTransition = NavTransition()
        ){
            SplashScreen(
                navigateToStarterScreen = {
                    navigator.navigate(EMAIL)
                }
            )
        }
        scene(
            route = EMAIL,
            navTransition = NavTransition()
        ){
            EmailScreen(
                navigator = navigator
            )
        }
        scene(
            route = SMS,
            navTransition = NavTransition()
        ){
            SmsScreen(
                navigator = navigator
            )
        }
    }
}