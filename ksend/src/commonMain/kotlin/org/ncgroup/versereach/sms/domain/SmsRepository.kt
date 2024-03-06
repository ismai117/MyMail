package org.ncgroup.versereach.sms.domain

import org.ncgroup.versereach.utils.ResultState
import kotlinx.coroutines.flow.Flow


interface SmsRepository {
    suspend fun sendSMS(
         recipient: String,
         body: String
    ): Flow<ResultState<Unit>>
}