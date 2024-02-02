package org.ncgroup.mymail.email.domain.repository

import org.ncgroup.mymail.utils.ResultState
import kotlinx.coroutines.flow.Flow
import email.service.Recipient

interface EmailRepository {
    suspend fun sendEmail(
        recipients: List<Recipient>,
        subject: String,
        content: String
    ): Flow<ResultState<Unit>>
}