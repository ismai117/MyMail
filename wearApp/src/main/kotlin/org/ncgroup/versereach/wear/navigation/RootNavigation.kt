package org.ncgroup.versereach.wear.navigation

import androidx.compose.runtime.Composable
import com.ncgroup.versereach.gemini.GeminiScreenModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import moe.tlaster.precompose.viewmodel.viewModel
import org.ncgroup.versereach.email.di.EmailModule
import org.ncgroup.versereach.email.presentation.EmailScreenModel
import org.ncgroup.versereach.sms.di.SmsModule
import org.ncgroup.versereach.sms.presentation.SmsScreenModel
import org.ncgroup.versereach.splash.SplashScreen
import org.ncgroup.versereach.wear.email.EmailScreen
import org.ncgroup.versereach.wear.sms.SmsScreen
import org.ncgroup.versereach.wear.starter.StarterScreen


private const val SPLASH = "splash_screen"
private const val STARTER = "starter_screen"
private const val EMAIL = "email_screen"
private const val SMS = "sms_screen"

@Composable
fun RootNavigation(
    navigator: Navigator
){

    val emailViewModel = viewModel(EmailScreenModel::class) {
        EmailScreenModel(emailRepository = EmailModule.emailRepository)
    }

    val smsViewModel = viewModel(SmsScreenModel::class) {
        SmsScreenModel(smsRepository = SmsModule.smsRepository)
    }

    val geminiViewModel = viewModel(GeminiScreenModel::class){
        GeminiScreenModel()
    }

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
                    navigator.navigate(STARTER)
                }
            )
        }
        scene(
            route = STARTER,
            navTransition = NavTransition()
        ){
            StarterScreen(
                navigateToEmailScreen = {
                    navigator.navigate(EMAIL)
                },
                navigateToSMSScreen = {
                    navigator.navigate(SMS)
                }
            )
        }
        scene(
            route = EMAIL,
            navTransition = NavTransition()
        ){
            val emailState = emailViewModel.state
            val geminiState = geminiViewModel.state
            EmailScreen(
                navigator = navigator,
                emailState = emailState,
                emailEvent = {
                    emailViewModel.onEvent(it)
                },
                recipients = emailViewModel.recipients,
                addRecipient = {
                    emailViewModel.addRecipient(it)
                },
                geminiState = geminiState,
                geminiEvent = {
                    geminiViewModel.onEvent(it)
                }
            )
        }
        scene(
            route = SMS,
            navTransition = NavTransition()
        ){
            val smsState = smsViewModel.state
            val geminiState = geminiViewModel.state
            SmsScreen(
                navigator = navigator,
                smsState = smsState,
                smsEvent = {
                    smsViewModel.onEvent(it)
                },
                geminiState = geminiState,
                geminiEvent = {
                    geminiViewModel.onEvent(it)
                },
                clearGeminiErrorMessage = {
                    geminiViewModel.clearErrorMessage()
                }
            )
        }
    }
}