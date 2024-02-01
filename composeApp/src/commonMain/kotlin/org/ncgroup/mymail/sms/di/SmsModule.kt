package org.ncgroup.mymail.sms.di


import org.ncgroup.mymail.sms.data.repository.SmsRepositoryImpl
import org.ncgroup.mymail.sms.domain.SmsRepository


object SmsModule {
    val smsRepository: SmsRepository by lazy {
        SmsRepositoryImpl()
    }
}