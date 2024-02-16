package org.ncgroup.versereach.wear.email

import KottieAnimation
import KottieCompositionSpec
import VerseReach.wearApp.BuildConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import animateKottieCompositionAsState
import com.ncgroup.versereach.gemini.GeminiEvent
import com.ncgroup.versereach.gemini.GeminiState
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.ncgroup.versereach.email.presentation.EmailEvent
import org.ncgroup.versereach.email.presentation.EmailState
import org.ncgroup.versereach.wear.ProgressBar
import rememberKottieComposition


@Preview(showBackground = true)
@Composable
fun EmailScreenPreview() {
    EmailScreen(
        navigator = rememberNavigator(),
        emailState = EmailState(),
        emailEvent = {},
        recipients = listOf(),
        addRecipient = {},
        geminiState = GeminiState(),
        geminiEvent = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    recipients: List<String>,
    addRecipient: (String) -> Unit,
    emailState: EmailState,
    emailEvent: (EmailEvent) -> Unit,
    geminiState: GeminiState,
    geminiEvent: (GeminiEvent) -> Unit
) {

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.Url("https://lottie.host/dd09ef53-b150-4c81-a3e1-b5516e940c31/GY604Ofcp4.json")
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        speed = 1f,
        iterations = 1,
        isPlaying = emailState.status
    )

    var enableErrorMessagePopUp by remember { mutableStateOf(false) }

    var geminiEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(
        emailState.recipientsError,
        emailState.subjectError,
        emailState.contentError
    ) {
        when {
            emailState.recipientsError.isNotBlank() -> {
                enableErrorMessagePopUp = true
            }

            emailState.subjectError.isNotBlank() -> {
                enableErrorMessagePopUp = true
            }

            emailState.contentError.isNotBlank() -> {
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
                emailEvent(EmailEvent.CONTENT(geminiState.response))
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
                        value = "",
                        onValueChange = {},
                        decorationBox = {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = modifier
                                        .fillMaxSize()
                                ) {
                                    Text(
                                        text = "From",
                                        color = Color.DarkGray,
                                        fontSize = 8.sp,
                                        modifier = modifier
                                            .padding(top = 8.dp, start = 16.dp)
                                            .align(Alignment.TopStart)
//                                            .border(width = 1.dp, color = Color.Black)
                                    )
                                    Box(
                                        modifier = modifier
                                            .padding(start = 12.dp)
                                            .align(Alignment.CenterStart)
//                                            .border(width = 1.dp, color = Color.Black)
                                    ) {
                                        Text(
                                            text = BuildConfig.SENDER_EMAIL_ADDRESS,
                                            color = Color.DarkGray,
                                            fontSize = 12.sp,
                                        )
                                    }
                                }
                            }
                        },
                        modifier = modifier
                            .padding(top = 40.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(24.dp)),
                        readOnly = true,
                    )

                    BasicTextField(
                        value = emailState.recipient,
                        onValueChange = {
                            emailEvent(EmailEvent.RECIPIENT(it))
                        },
                        textStyle = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 12.sp,
                        ),
                        decorationBox = { innerField ->
                            BadgedBox(
                                badge = {
                                    if (recipients.isNotEmpty()){
                                        Badge {
                                            Text(text = "${recipients.count()}")
                                        }
                                    }
                                }
                            ) {
                                Row(
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (emailState.recipient.isBlank()) {
                                        Box(
                                            modifier = modifier
                                                .fillMaxSize()
                                        ) {
                                            Text(
                                                text = "Recipient",
                                                color = Color.DarkGray,
                                                fontSize = 12.sp,
                                                modifier = modifier
                                                    .padding(start = 12.dp)
                                                    .align(Alignment.CenterStart)
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                text = "Recipient",
                                                color = Color.DarkGray,
                                                fontSize = 8.sp,
                                                modifier = modifier
                                                    .padding(top = 8.dp, start = 12.dp)
                                                    .align(Alignment.TopStart)
                                            )
                                            Box(
                                                modifier = modifier
                                                    .padding(start = 12.dp)
                                                    .align(Alignment.CenterStart)
                                            ) {
                                                innerField()
                                            }
                                            Box(
                                                modifier = modifier
                                                    .padding(end = 12.dp)
                                                    .align(Alignment.CenterEnd)
                                                    .clickable {
                                                        addRecipient(emailState.recipient)
                                                    }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "add recipient"
                                                )
                                            }
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
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        )
                    )


                    BasicTextField(
                        value = emailState.subject,
                        onValueChange = {
                            emailEvent(EmailEvent.SUBJECT(it))
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
                                Row(
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (emailState.subject.isBlank()) {
                                        Text(
                                            text = "Subject",
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
                                                text = "Subject",
                                                color = Color.DarkGray,
                                                fontSize = 8.sp,
                                                modifier = modifier
                                                    .padding(top = 8.dp, start = 12.dp)
                                                    .align(Alignment.TopStart)
//                                                    .border(width = 1.dp, color = Color.Black)
                                            )
                                            Box(
                                                modifier = modifier
                                                    .padding(start = 12.dp)
                                                    .align(Alignment.CenterStart)
//                                                    .border(width = 1.dp, color = Color.Black)
                                            ) {
                                                innerField()
                                            }
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
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )


                    BasicTextField(
                        value = emailState.content,
                        onValueChange = {
                            emailEvent(EmailEvent.CONTENT(it))
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
                                if (emailState.content.isBlank()) {
                                    Text(
                                        text = "Content",
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
                                            text = "Content",
                                            color = Color.DarkGray,
                                            fontSize = 8.sp,
                                            modifier = modifier
                                                .padding(top = 8.dp, start = 12.dp)
                                                .align(Alignment.TopStart)
//                                                .border(width = 1.dp, color = Color.Black)
                                        )
                                        Box(
                                            modifier = modifier
                                                .padding(start = 12.dp)
                                                .align(Alignment.CenterStart)
//                                                .border(width = 1.dp, color = Color.Black)
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
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    Button(
                        onClick = {
                                  emailEvent(EmailEvent.SUBMIT)
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

                ProgressBar(isLoading = emailState.isLoading)

                if (emailState.status) {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        KottieAnimation(
                            composition = composition,
                            progress = { animationState.progress },
                            modifier = modifier
                                .size(140.dp)
                        )
                    }
                }

            }
        }
    )



    if (geminiEnabled) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Alert(
                title = {
                    Text(
                        text = "Generate Email Template",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
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
                            fontSize = 12.sp,
                            modifier = modifier
                                .padding(start = 12.dp, end = 12.dp)
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
            )
        }
    }

    if (enableErrorMessagePopUp) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Alert(
                title = {
                    when {
                        emailState.recipientsError.isNotBlank() -> {
                            Text(
                                text = emailState.recipientsError,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        emailState.subjectError.isNotBlank() -> {
                            Text(
                                text = emailState.subjectError,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        emailState.contentError.isNotBlank() -> {
                            Text(
                                text = emailState.contentError,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        geminiState.promptError.isNotBlank() -> {
                            Text(
                                text = geminiState.promptError,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        geminiState.error.isNotBlank() -> {
                            Text(
                                text = geminiState.error,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                },
                negativeButton = {},
                positiveButton = {
                    Box(
                        modifier = modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Button(
                            onClick = {
                                emailEvent(EmailEvent.CLEAR_ERROR_MESSAGES)
                                enableErrorMessagePopUp = false
                            }
                        ) {
                            Text(
                                text = "OK",
                                fontSize = 12.sp
                            )
                        }
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
            emailEvent(EmailEvent.RESET_UI_STATE)
        }
    }

}