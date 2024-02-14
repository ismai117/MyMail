package com.ncgroup.versereach.gemini

import VerseReach.common_gemini.BuildConfig
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ncgroup.versereach.utils.validation.ValidatePrompt
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import moe.tlaster.precompose.viewmodel.ViewModel


class GeminiViewModel : ViewModel() {

    var state by mutableStateOf(GeminiState())

    private val validatePrompt = ValidatePrompt()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun onEvent(event: GeminiEvent){
        when(event){
            is GeminiEvent.PROMPT -> {
                state = state.copy(prompt = event.value)
            }
            is GeminiEvent.SUBMIT -> {
                generate()
            }
        }
    }

    private fun generate(){

        val promptResult = validatePrompt(state.prompt)

        if (!promptResult.successful){
            state = state.copy(
                promptError = promptResult.errorMessage
            )
            return
        }

        state = state.copy(isLoading = true, status = false, response = "", error = "")

//        viewModelScope.launch {
//            runCatching {
//                generativeModel.generateContent(content { text(state.prompt) })
//            }.onSuccess {
//                if (it.text?.isNotBlank() == true){
//                    state = state.copy(
//                        isLoading = false,
//                        status = true,
//                        response = it.text!!
//                    )
//                    println("gemini response: ${it.text}")
//                }
//            }.onFailure {
//                state = state.copy(
//                    isLoading = false,
//                    error = it.message.orEmpty()
//                )
//                println("gemini error: ${it.message}")
//            }
//        }

    }

    fun clearErrorMessage(){
        state = state.copy(promptError = "")
    }

}

