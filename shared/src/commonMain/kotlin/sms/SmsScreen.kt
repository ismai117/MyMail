package org.ncgroup.versereach.sms


import KottieAnimation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.ncgroup.versereach.gemini.GeminiEvent
import com.ncgroup.versereach.gemini.GeminiScreenModel
import com.ncgroup.versereach.gemini.GeminiState
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.ncgroup.versereach.sharedComponents.ProgressBar
import org.ncgroup.versereach.sharedComponents.TopBar
import org.ncgroup.versereach.sms.di.SmsModule
import org.ncgroup.versereach.sms.presentation.SmsEvent
import org.ncgroup.versereach.sms.presentation.SmsScreenModel
import org.ncgroup.versereach.sms.presentation.SmsState



object SmsScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "SMS"
            val icon = rememberVectorPainter(image = Icons.Default.Sms)
            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {

        val smsScreenModel = rememberScreenModel {
            SmsScreenModel(smsRepository = SmsModule.smsRepository)
        }

        val smsState = smsScreenModel.state

        val geminiScreenModel = rememberScreenModel {
            GeminiScreenModel()
        }

        val geminiState = geminiScreenModel.state

        SmsScreenContent(
            smsState = smsState,
            smsOnEvent = {
                smsScreenModel.onEvent(it)
            },
            geminiState = geminiState,
            geminiOnEvent = {
                geminiScreenModel.onEvent(it)
            }
        )

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SmsScreenContent(
    modifier: Modifier = Modifier,
    smsState: SmsState,
    smsOnEvent: (SmsEvent) -> Unit,
    geminiState: GeminiState,
    geminiOnEvent:  (GeminiEvent) -> Unit
) {


    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.Url("https://lottie.host/dd09ef53-b150-4c81-a3e1-b5516e940c31/GY604Ofcp4.json")
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        speed = 1f,
        iterations = 1,
        isPlaying = smsState.status
    )

    val (recipientRequester, bodyRequester, promptRequester) = FocusRequester.createRefs()
    val focusManager = LocalFocusManager.current

//    LaunchedEffect(Unit) {
//        recipientRequester.requestFocus()
//    }

    var enableErrorMessagePopUp by remember { mutableStateOf(false) }

    var geminiEnabled by remember { mutableStateOf(false) }

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

    LaunchedEffect(
        geminiState.isLoading,
        geminiState.status,
        geminiState.error,
        geminiState.promptError
    ) {
        when {
            geminiState.isLoading -> {
                geminiEnabled = true
            }

            geminiState.status -> {
                geminiEnabled = false
                smsOnEvent(SmsEvent.BODY(geminiState.response))
            }

            geminiState.error.isNotBlank() -> {
                enableErrorMessagePopUp = true
            }

            geminiState.promptError.isNotBlank() -> {
                geminiEnabled = false
                enableErrorMessagePopUp = true
            }
        }
    }

//    LaunchedEffect(geminiEnabled) {
//        if (geminiEnabled) {
//            promptRequester.requestFocus()
//        }
//    }

    Scaffold(
        topBar = {
            TopBar(
                title = "",
                enableGemini = {
                    geminiEnabled = true
                }
            )
        },
        bottomBar = {
//            BottomBar(
//                navigator = navigator
//            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    smsOnEvent(SmsEvent.SUBMIT)
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
                            smsOnEvent(SmsEvent.RECIPIENT(it))
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

                    HorizontalDivider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )

                    TextField(
                        value = smsState.body,
                        onValueChange = {
                            smsOnEvent(SmsEvent.BODY(it))
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

                if (geminiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = modifier.align(Alignment.Center)
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

    if (geminiEnabled) {
        Box(
            modifier = modifier
                .wrapContentSize()
        ) {
            AlertDialog(
                onDismissRequest = {
                    geminiEnabled = false
                },
                title = {
                    Text(
                        text = "Generate SMS Template",
                        fontSize = 14.sp
                    )
                },
                text = {
                    TextField(
                        value = geminiState.prompt,
                        onValueChange = {
                            geminiOnEvent(
                                GeminiEvent.PROMPT(it)
                            )
                        },
                        modifier = modifier
                            .focusRequester(promptRequester),
                        textStyle = TextStyle(
                            fontSize = 15.sp
                        ),
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
                },
                dismissButton = {
                    Button(
                        onClick = {
                            geminiEnabled = false
                        }
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 12.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            geminiOnEvent(
                                GeminiEvent.SUBMIT
                            )
                        }
                    ) {
                        Text(
                            text = "Generate",
                            fontSize = 12.sp
                        )
                    }
                }
            )
        }
    }

    if (enableErrorMessagePopUp) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        geminiOnEvent(GeminiEvent.CLEAR_MESSAGE)
                        smsOnEvent(SmsEvent.CLEAR_ERROR_MESSAGES)
                        enableErrorMessagePopUp = false
                    }
                ) {
                    Text(
                        text = "OK"
                    )
                }
            },
            text = {
                when {
                    smsState.recipientError.isNotBlank() -> {
                        Text(
                            text = smsState.recipientError
                        )
                    }

                    smsState.bodyError.isNotBlank() -> {
                        Text(
                            text = smsState.bodyError
                        )
                    }

                    geminiState.promptError.isNotBlank() -> {
                        Text(
                            text = geminiState.promptError
                        )
                    }

                    geminiState.error.isNotBlank() -> {
                        Text(
                            text = geminiState.error
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
            smsOnEvent(SmsEvent.RESET_UI_STATE)
        }
    }

}