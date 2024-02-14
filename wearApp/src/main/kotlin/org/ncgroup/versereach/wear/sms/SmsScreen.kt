package org.ncgroup.versereach.wear.sms

import KottieAnimation
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import animateKottieCompositionAsState
import com.ncgroup.versereach.gemini.GeminiEvent
import com.ncgroup.versereach.gemini.GeminiState
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.ncgroup.versereach.sms.presentation.SmsEvent
import org.ncgroup.versereach.sms.presentation.SmsState
import org.ncgroup.versereach.wear.ProgressBar
import rememberKottieComposition


@Preview(showBackground = true)
@Composable
fun SmsScreenPreview() {
    SmsScreen(
        navigator = rememberNavigator(),
        smsState = SmsState(),
        smsEvent = {},
        geminiState = GeminiState(),
        geminiEvent = {},
        clearGeminiErrorMessage = {}
    )
}

@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    smsState: SmsState,
    smsEvent: (SmsEvent) -> Unit,
    geminiState: GeminiState,
    geminiEvent: (GeminiEvent) -> Unit,
    clearGeminiErrorMessage: () -> Unit
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
                smsEvent(SmsEvent.BODY(geminiState.response))
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


    Scaffold(
        content = {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = modifier
                        .padding(start = 24.dp, end = 24.dp)
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    BasicTextField(
                        value = smsState.recipient,
                        onValueChange = {
                            smsEvent(SmsEvent.RECIPIENT(it))
                        },
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 12.sp,
                        ),
                        decorationBox = { innerField ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (smsState.recipient.isBlank()) {
                                    Text(
                                        text = "Recipient",
                                        color = Color.DarkGray,
                                        fontSize = 12.sp,
                                        modifier = modifier.padding(start = 12.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = modifier
                                            .fillMaxSize()
                                    ) {
                                        Text(
                                            text = "Recipient",
                                            color = Color.DarkGray,
                                            fontSize = 8.sp,
                                            modifier = modifier
                                                .padding(top = 8.dp, start = 12.dp)
                                                .align(Alignment.TopStart)
                                                .border(width = 1.dp, color = Color.Black)
                                        )
                                        Box(
                                            modifier = modifier
                                                .padding(start = 12.dp)
                                                .align(Alignment.CenterStart)
                                                .border(width = 1.dp, color = Color.Black)
                                        ) {
                                            innerField()
                                        }
                                    }
                                }
                            }
                        },
                        modifier = modifier
                            .padding(top = 40.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(24.dp)),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )

                    BasicTextField(
                        value = smsState.body,
                        onValueChange = {
                            smsEvent(SmsEvent.BODY(it))
                        },
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 12.sp,
                        ),
                        decorationBox = { innerField ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (smsState.body.isBlank()) {
                                    Text(
                                        text = "Body",
                                        color = Color.DarkGray,
                                        fontSize = 12.sp,
                                        modifier = modifier.padding(start = 12.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = modifier
                                            .fillMaxSize()
                                    ) {
                                        Text(
                                            text = "Body",
                                            color = Color.DarkGray,
                                            fontSize = 8.sp,
                                            modifier = modifier
                                                .padding(top = 8.dp, start = 12.dp)
                                                .align(Alignment.TopStart)
                                                .border(width = 1.dp, color = Color.Black)
                                        )
                                        Box(
                                            modifier = modifier
                                                .padding(start = 12.dp)
                                                .align(Alignment.CenterStart)
                                                .border(width = 1.dp, color = Color.Black)
                                        ) {
                                            innerField()
                                        }
                                    }
                                }
                            }
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(24.dp)),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )

                    Button(
                        onClick = {

                        },
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "SEND",
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = {
                            navigator.popBackStack()
                        },
                        modifier = modifier
                            .padding(bottom = 40.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "BACK",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

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
                        backgroundColor = MaterialTheme.colors.onSurfaceVariant
                    )
                }

            }
        }
    )


    if (geminiEnabled){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Alert(
                title = {
                    Text(
                        text = "Generate SMS Template",
                        fontSize = 12.sp
                    )
                },
                negativeButton = {
                    Button(
                        onClick = {
                            geminiEnabled = false
                        },
                        modifier = modifier.width(70.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 12.sp
                        )
                    }
                },
                positiveButton = {
                    Button(
                        onClick = {
                            geminiEvent(
                                GeminiEvent.SUBMIT
                            )
                        },
                        modifier = modifier.width(70.dp)
                    ) {
                        Text(
                            text = "Generate",
                            fontSize = 12.sp
                        )
                    }
                },
                content = {
                    BasicTextField(
                        value = geminiState.prompt,
                        onValueChange = {
                            geminiEvent(
                                GeminiEvent.PROMPT(it)
                            )
                        },
                        decorationBox = { innerField ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = modifier
                                        .fillMaxSize()
                                ) {
                                    Box(
                                        modifier = modifier
                                            .padding(start = 12.dp)
                                            .align(Alignment.CenterStart)
                                    ) {
                                        innerField()
                                    }
                                }
                            }
                        },
                        textStyle = TextStyle(
                            fontSize = 15.sp
                        ),
                        modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(24.dp)),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                },
                modifier = modifier
                    .clip(RoundedCornerShape(24.dp)),
                backgroundColor = MaterialTheme.colors.onSurface
            )
        }
    }

    if (enableErrorMessagePopUp){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Alert(
                title = {
                    when {
                        smsState.recipientError.isBlank() -> {
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
                },
                negativeButton = {
                    Button(
                        onClick = {
                            enableErrorMessagePopUp = false
                        },
                        modifier = modifier.width(70.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 12.sp
                        )
                    }
                },
                positiveButton = {
                    Button(
                        onClick = {
                            clearGeminiErrorMessage()
                            smsEvent(SmsEvent.CLEAR_ERROR_MESSAGES)
                            enableErrorMessagePopUp = false
                        },
                        modifier = modifier.width(70.dp)
                    ) {
                        Text(
                            text = "OK",
                            fontSize = 12.sp
                        )
                    }
                },
                modifier = modifier
                    .clip(RoundedCornerShape(24.dp)),
                backgroundColor = MaterialTheme.colors.onSurface
            )
        }
    }


    LaunchedEffect(
        key1 = animationState.isPlaying
    ) {
        if (animationState.isPlaying) {
            println("Animation Playing")
        }
        if (animationState.isCompleted) {
            println("Animation Completed")
            smsEvent(SmsEvent.RESET_UI_STATE)
        }
    }

}