package org.ncgroup.versereach.sms.di


import org.ncgroup.versereach.sms.data.repository.SmsRepositoryImpl
import org.ncgroup.versereach.sms.domain.SmsRepository


object SmsModule {
    val smsRepository: SmsRepository by lazy {
        SmsRepositoryImpl()
    }
}