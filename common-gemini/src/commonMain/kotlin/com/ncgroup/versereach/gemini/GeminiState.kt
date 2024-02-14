package com.ncgroup.versereach.gemini


data class GeminiState(

    val isLoading: Boolean = false,
    val status: Boolean = false,
    val error: String = "",

    val prompt: String = "",
    val promptError: String = "",

    val response: String = ""

)
