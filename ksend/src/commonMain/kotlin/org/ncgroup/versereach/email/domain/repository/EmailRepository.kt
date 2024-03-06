package org.ncgroup.versereach.email.domain.repository

import org.ncgroup.versereach.utils.ResultState
import kotlinx.coroutines.flow.Flow
import email.service.Recipient

interface EmailRepository {
    suspend fun sendEmail(
        recipients: List<Recipient>,
        subject: String,
        content: String
    ): Flow<ResultState<Unit>>
}