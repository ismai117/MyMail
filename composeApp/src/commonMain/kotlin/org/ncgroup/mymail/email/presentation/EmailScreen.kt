package org.ncgroup.mymail.email.presentation


import KottieAnimation
import MyMail.composeApp.BuildConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import animateKottieCompositionAsState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import org.ncgroup.mymail.email.di.EmailModule
import moe.tlaster.precompose.navigation.Navigator
import org.ncgroup.mymail.gemini.GeminiEvent
import org.ncgroup.mymail.gemini.GeminiViewModel
import org.ncgroup.mymail.sharedComponents.BottomBar
import org.ncgroup.mymail.sharedComponents.ProgressBar
import org.ncgroup.mymail.sharedComponents.TopBar
import rememberKottieComposition


// 7599946274

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
)
@Composable
fun EmailScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator
) {

    val emailViewModel: EmailViewModel = getViewModel(
        key = "email_app_screen",
        factory = viewModelFactory {
            EmailViewModel(emailRepository = EmailModule.emailRepository)
        }
    )

    val emailState = emailViewModel.state

    val geminiViewModel: GeminiViewModel = getViewModel(
        key = "email_app_screen_gemini",
        factory = viewModelFactory {
            GeminiViewModel()
        }
    )

    val geminiState = geminiViewModel.state

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.Url("https://lottie.host/dd09ef53-b150-4c81-a3e1-b5516e940c31/GY604Ofcp4.json")
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        speed = 1f,
        iterations = 1,
        isPlaying = emailState.status
    )

    val (recipientRequester, subjectRequester, contentRequester) = FocusRequester.createRefs()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        recipientRequester.requestFocus()
    }

    var enableErrorMessagePopUp by remember { mutableStateOf(false) }

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

    var geminiEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(geminiState.status){
        if (geminiState.status){
            println("success")
            geminiEnabled = false
            emailViewModel.onEvent(EmailEvent.CONTENT(geminiState.response))
        }
    }

    LaunchedEffect(geminiState.isLoading){
        if (geminiState.isLoading){
            geminiEnabled = false
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
            BottomBar(
                navigator = navigator
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    emailViewModel.onEvent(EmailEvent.SUBMIT)
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

                        Divider(
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
                                        emailViewModel.onEvent(EmailEvent.RECIPIENT(it))
                                    },
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .focusRequester(recipientRequester),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                if (emailState.recipient.isNotBlank()) {
                                                    emailViewModel.addRecipient(emailState.recipient)
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
                                    emailViewModel.recipients
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

                    Divider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )

                    TextField(
                        value = emailState.subject,
                        onValueChange = {
                            emailViewModel.onEvent(EmailEvent.SUBJECT(it))
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

                    Divider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 3.dp
                    )

                    TextField(
                        value = emailState.content,
                        onValueChange = {
                            emailViewModel.onEvent(EmailEvent.CONTENT(it))
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

                if (geminiState.isLoading){
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

    if (geminiEnabled){
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
                           geminiViewModel.onEvent(
                               GeminiEvent.PROMPT(it)
                           )
                       },
                       textStyle = TextStyle(
                           fontSize = 15.sp
                       )
                   )
               },
               dismissButton = {
                   Button(
                       onClick = {
                           geminiEnabled = false
                       }
                   ){
                       Text(
                           text = "Cancel",
                           fontSize = 12.sp
                       )
                   }
               },
               confirmButton = {
                   Button(
                       onClick = {
                           geminiViewModel.onEvent(
                               GeminiEvent.SUBMIT
                           )
                       }
                   ){
                       Text(
                           text = "Generate",
                           fontSize = 12.sp
                       )
                   }
               }
           )
       }
    }

    if (enableErrorMessagePopUp){
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
               Button(
                   onClick = {
                       emailViewModel.onEvent(EmailEvent.CLEAR_ERROR_MESSAGES)
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
            emailViewModel.onEvent(EmailEvent.RESET_UI_STATE)
        }
    }

}

