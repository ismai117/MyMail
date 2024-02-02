package org.ncgroup.mymail.sms.domain

import org.ncgroup.mymail.utils.ResultState
import kotlinx.coroutines.flow.Flow


interface SmsRepository {
    suspend fun sendSMS(
         recipient: String,
         body: String
    ): Flow<ResultState<Unit>>
}