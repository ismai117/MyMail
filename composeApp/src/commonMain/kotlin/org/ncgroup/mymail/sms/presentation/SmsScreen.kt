package org.ncgroup.mymail.sms.presentation


import KottieAnimation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import animateKottieCompositionAsState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import moe.tlaster.precompose.navigation.Navigator
import org.ncgroup.mymail.sharedComponents.BottomBar
import org.ncgroup.mymail.sharedComponents.ProgressBar
import org.ncgroup.mymail.sharedComponents.TopBar
import org.ncgroup.mymail.sms.di.SmsModule
import org.ncgroup.mymail.theme.AppTheme
import rememberKottieComposition


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator
) {

    val smsViewModel: SmsViewModel = getViewModel(
        key = "sms_app_screen",
        factory = viewModelFactory {
            SmsViewModel(smsRepository = SmsModule.smsRepository)
        }
    )

    val smsState = smsViewModel.state

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.Url("https://lottie.host/dd09ef53-b150-4c81-a3e1-b5516e940c31/GY604Ofcp4.json")
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        speed = 1f,
        iterations = 1,
        isPlaying = smsState.status
    )

    val (recipientRequester, bodyRequester) = FocusRequester.createRefs()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit){
        recipientRequester.requestFocus()
    }

    var enableErrorMessagePopUp by remember { mutableStateOf(false) }

    LaunchedEffect(
        smsState.recipientError,
        smsState.bodyError
    ) {
        when {
            smsState.recipientError.isNotBlank() -> {
                enableErrorMessagePopUp = true
            }

            smsState.bodyError.isNotBlank() -> {
                enableErrorMessagePopUp = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "",
                enableGemini = {

                }
            )
        },
        bottomBar = {
            BottomBar(
                navigator = navigator
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    smsViewModel.onEvent(SmsEvent.SUBMIT)
                },
                modifier = modifier
                    .width(70.dp)
            ) {
                Text(
                    text = "Send"
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {

                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TextField(
                        value = smsState.recipient,
                        onValueChange = {
                            smsViewModel.onEvent(SmsEvent.RECIPIENT(it))
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .focusRequester(recipientRequester),
                        label = {
                            Text(
                                text = "Recipient"
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Divider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )

                    TextField(
                        value = smsState.body,
                        onValueChange = {
                            smsViewModel.onEvent(SmsEvent.BODY(it))
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .focusRequester(bodyRequester),
                        label = {
                            Text(
                                text = "Body"
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                }

                ProgressBar(isLoading = smsState.isLoading)

                if (smsState.status) {
                    KottieAnimation(
                        composition = composition,
                        progress = { animationState.progress },
                        modifier = modifier
                            .fillMaxSize(),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

            }
        }
    )

    if (enableErrorMessagePopUp){
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        smsViewModel.onEvent(SmsEvent.CLEAR_ERROR_MESSAGES)
                        enableErrorMessagePopUp = false
                    }
                ){
                    Text(
                        text = "OK"
                    )
                }
            },
            text = {
                when {
                    smsState.recipientError.isNotBlank() -> {
                        Text(
                            text =  smsState.recipientError
                        )
                    }

                    smsState.bodyError.isNotBlank() -> {
                        Text(
                            text = smsState.bodyError
                        )
                    }
                }
            }
        )
    }


    LaunchedEffect(
        key1 = animationState.isPlaying
    ) {
        if (animationState.isPlaying) {
            println("Animation Playing")
        }
        if (animationState.isCompleted) {
            println("Animation Completed")
            smsViewModel.onEvent(SmsEvent.RESET_UI_STATE)
        }
    }

}