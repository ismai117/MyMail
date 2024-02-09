package org.ncgroup.mymail.sms.presentation

import org.ncgroup.mymail.email.presentation.EmailEvent


sealed interface SmsEvent {
    class RECIPIENT(val recipient: String) :SmsEvent
    class BODY(val body: String) : SmsEvent
   object SUBMIT : SmsEvent
    data object CLEAR_ERROR_MESSAGES : SmsEvent
    data object RESET_UI_STATE : SmsEvent
}