package org.ncgroup.mymail.email.di

import org.ncgroup.mymail.email.data.repository.EmailRepositoryImpl
import org.ncgroup.mymail.email.domain.repository.EmailRepository


object EmailModule {
    val emailRepository: EmailRepository by lazy {
        EmailRepositoryImpl()
    }
}