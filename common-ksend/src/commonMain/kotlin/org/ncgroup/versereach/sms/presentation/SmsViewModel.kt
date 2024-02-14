package org.ncgroup.versereach.sms.presentation

import org.ncgroup.versereach.utils.ResultState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.ncgroup.versereach.sms.domain.SmsRepository
import validation.sms.ValidateBody
import validation.sms.ValidateRecipientPhoneNumber

class SmsViewModel(
    private val smsRepository: SmsRepository
) : ViewModel() {

    val validateRecipientPhoneNumber = ValidateRecipientPhoneNumber()
    val validateBody = ValidateBody()

    var state by mutableStateOf(SmsState())

    fun onEvent(event: SmsEvent){
        when(event){
            is SmsEvent.RECIPIENT -> {
                state = state.copy(recipient = event.recipient)
            }
            is SmsEvent.BODY -> {
                state = state.copy(body = event.body)
            }
            is SmsEvent.SUBMIT -> {
                sendSms()
            }
            is SmsEvent.CLEAR_ERROR_MESSAGES -> {
                clearErrorMessages()
            }
            is SmsEvent.RESET_UI_STATE -> {
                resetUIState()
            }
        }
    }

    private fun sendSms() {

        val recipientResult = validateRecipientPhoneNumber(phoneNumber = state.recipient)
        val bodyResult = validateBody(body = state.body)

        val hasError = listOf(
            recipientResult,
            bodyResult
        ).any { !it.successful }

        if (hasError){
            state = state.copy(
                recipientError = recipientResult.errorMessage,
                bodyError = bodyResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            smsRepository.sendSMS(
                recipient = state.recipient,
                body = state.body
            ).collect { result ->
                state = state.copy(isLoading = false, status = false, error = "")
                when (result) {
                    is ResultState.Loading -> {
                        state = state.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        state = state.copy(
                            isLoading = false,
                            status = true
                        )
                    }

                    is ResultState.Error -> {
                        state = state.copy(
                            isLoading = false,
                            status = false,
                            error = result.message
                        )
                    }
                }
            }
        }

    }

    private fun resetUIState() {
        state = state.copy(
            status = false,
            recipient = "",
            body = ""
        )
    }

    private fun clearErrorMessages() {
        state = state.copy(
            recipientError = "",
            bodyError = ""
        )
    }

}