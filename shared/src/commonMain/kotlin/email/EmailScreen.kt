package org.ncgroup.versereach.email


import KottieAnimation
import VerseReach.shared.BuildConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
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
import androidx.compose.ui.text.input.KeyboardType
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
import org.ncgroup.versereach.email.di.EmailModule
import org.ncgroup.versereach.email.presentation.EmailEvent
import org.ncgroup.versereach.email.presentation.EmailScreenModel
import org.ncgroup.versereach.email.presentation.EmailState
import org.ncgroup.versereach.sharedComponents.ProgressBar
import org.ncgroup.versereach.sharedComponents.TopBar


// 7599946274


object EmailScreen : Tab {

    override val options: TabOptions
    @Composable
    get() {
        val title = "EMAIL"
        val icon = rememberVectorPainter(image = Icons.Default.Email)
        return remember {
            TabOptions(
                index = 0u,
                title = title,
                icon = icon
            )
        }
    }

    @Composable
    override fun Content() {

        val emailScreenModel = rememberScreenModel {  EmailScreenModel(emailRepository = EmailModule.emailRepository) }
        val emailState =  emailScreenModel.state

        val geminiScreenModel = rememberScreenModel { GeminiScreenModel() }
        val geminiState = geminiScreenModel.state

        EmailScreenContent(
            emailState = emailState,
            emailOnEvent = {
                emailScreenModel.onEvent(it)
            },
            recipients = emailScreenModel.recipients,
            addRecipient = {
                 emailScreenModel.addRecipient(it)
            },
            geminiState = geminiState,
            geminiOnEvent = {
                geminiScreenModel.onEvent(it)
            }
        )

    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)
@Composable
fun EmailScreenContent(
    modifier: Modifier = Modifier,
    emailState: EmailState,
    emailOnEvent: (EmailEvent) -> Unit,
    recipients: List<String>,
    addRecipient: (String) -> Unit,
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
        isPlaying = emailState.status
    )

    val (recipientRequester, subjectRequester, contentRequester, promptRequester) = FocusRequester.createRefs()
    val focusManager = LocalFocusManager.current

//    LaunchedEffect(Unit) {
//        recipientRequester.requestFocus()
//    }

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
                emailOnEvent(EmailEvent.CONTENT(geminiState.response))
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

    LaunchedEffect(geminiEnabled){
        if (geminiEnabled){
            promptRequester.requestFocus()
        }
    }

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
                    emailOnEvent(EmailEvent.SUBMIT)
                },
                modifier = modifier
                    .padding(bottom = 24.dp)
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
                    .fillMaxSize()
            ) {

                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column {

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {

                            Box(
                                modifier = modifier
                                    .weight(0.18f)
//                                    .border(width = 1.dp, color = Color.Blue)
                            ) {
                                Box(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .heightIn(TextFieldDefaults.MinHeight)
                                        .align(Alignment.TopCenter)
                                ) {
                                    Text(
                                        text = "From",
                                        modifier = modifier
                                            .padding(start = 16.dp)
                                            .align(Alignment.CenterStart)
                                    )
                                }
                            }

                            Column(
                                modifier = modifier
                                    .weight(1f)
//                                    .border(width = 1.dp, color = Color.Blue)
                            ) {
                                TextField(
                                    value = BuildConfig.SENDER_EMAIL_ADDRESS,
                                    onValueChange = {},
                                    modifier = modifier.fillMaxWidth(),
                                    readOnly = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }

                        }

                        HorizontalDivider(
                            modifier = modifier.fillMaxWidth(),
                            thickness = 3.dp,
                            color = DividerDefaults.color
                        )

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {

                            Box(
                                modifier = modifier
                                    .weight(0.18f)
//                                    .border(width = 1.dp, color = Color.Blue)
                            ) {
                                Box(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .heightIn(TextFieldDefaults.MinHeight)
                                        .align(Alignment.TopCenter)
                                ) {
                                    Text(
                                        text = "To",
                                        modifier = modifier
                                            .padding(start = 16.dp)
                                            .align(Alignment.CenterStart)
                                    )
                                }
                            }

                            Column(
                                modifier = modifier
                                    .weight(1f)
//                                    .border(width = 1.dp, color = Color.Blue)
                            ) {
                                TextField(
                                    value = emailState.recipient,
                                    onValueChange = {
                                        emailOnEvent(EmailEvent.RECIPIENT(it))
                                    },
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .focusRequester(recipientRequester),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                if (emailState.recipient.isNotBlank()) {
                                                    addRecipient(emailState.recipient)
                                                }
                                            },
                                            enabled = emailState.recipient.isNotBlank()
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Add Recipient"
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
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

                                FlowRow(
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    recipients
                                        .forEach {
                                            SuggestionChip(
                                                onClick = {},
                                                label = {
                                                    Text(
                                                        text = it
                                                    )
                                                }
                                            )
                                        }
                                }
                            }

                        }

                    }

                    HorizontalDivider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )

                    TextField(
                        value = emailState.subject,
                        onValueChange = {
                            emailOnEvent(EmailEvent.SUBJECT(it))
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .focusRequester(subjectRequester),
                        placeholder = {
                            Text(
                                text = "Subject"
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
                        value = emailState.content,
                        onValueChange = {
                            emailOnEvent(EmailEvent.CONTENT(it))
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .focusRequester(contentRequester),
                        placeholder = {
                            Text(
                                text = "Content"
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

                ProgressBar(isLoading = emailState.isLoading)

                if (emailState.status) {
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
                        text = "Generate Email Template",
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
                            geminiOnEvent(GeminiEvent.SUBMIT)
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
                        emailOnEvent(EmailEvent.CLEAR_ERROR_MESSAGES)
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
                    emailState.recipientsError.isNotBlank() -> {
                        Text(
                            text = emailState.recipientsError
                        )
                    }

                    emailState.subjectError.isNotBlank() -> {
                        Text(
                            text = emailState.subjectError
                        )
                    }

                    emailState.contentError.isNotBlank() -> {
                        Text(
                            text = emailState.contentError
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
            emailOnEvent(EmailEvent.RESET_UI_STATE)
        }
    }

}
