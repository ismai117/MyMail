package org.ncgroup.mymail.gemini


sealed interface GeminiEvent {
    class PROMPT(val value: String) : GeminiEvent
    data object SUBMIT : GeminiEvent
}