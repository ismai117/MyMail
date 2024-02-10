package org.ncgroup.mymail.gemini

import VerseReach.composeApp.BuildConfig
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.launch


class GeminiViewModel : ViewModel() {

    var state by mutableStateOf(GeminiState())

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

        state = state.copy(isLoading = true, status = false, response = "", error = "")

        println("clicked!!")

        if (state.prompt.isBlank()){
            return
        }

        viewModelScope.launch {
            runCatching {
                generativeModel.generateContent(content { text(state.prompt) })
            }.onSuccess {
                if (it.text?.isNotBlank() == true){
                    state = state.copy(
                        isLoading = false,
                        status = true,
                        response = it.text!!
                    )
                    println("gemini response: ${it.text}")
                }
            }.onFailure {
                state = state.copy(
                    isLoading = false,
                    error = it.message.orEmpty()
                )
                println("gemini error: ${it.message}")
            }
        }

    }

}

