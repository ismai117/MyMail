package org.ncgroup.mymail.email.presentation


sealed interface EmailEvent {
    class RECIPIENT(val recipient: String) : EmailEvent
    class SUBJECT(val subject: String) : EmailEvent
    class CONTENT(val content: String) : EmailEvent
    data object SUBMIT : EmailEvent
    data object CLEAR_ERROR_MESSAGES : EmailEvent
    data object RESET_UI_STATE : EmailEvent
}

