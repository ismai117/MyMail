package org.ncgroup.versereach.sms.presentation


sealed interface SmsEvent {
    class RECIPIENT(val recipient: String) :SmsEvent
    class BODY(val body: String) : SmsEvent
   object SUBMIT : SmsEvent
    data object CLEAR_ERROR_MESSAGES : SmsEvent
    data object RESET_UI_STATE : SmsEvent
}