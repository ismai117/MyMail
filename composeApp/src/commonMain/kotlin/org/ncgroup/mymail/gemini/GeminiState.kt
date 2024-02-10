package org.ncgroup.mymail.gemini


data class GeminiState(

    val isLoading: Boolean = false,
    val status: Boolean = false,
    val error: String = "",

    val prompt: String = "",
    val response: String = ""

)
