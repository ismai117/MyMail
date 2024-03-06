package org.ncgroup.versereach.email.di

import org.ncgroup.versereach.email.data.repository.EmailRepositoryImpl
import org.ncgroup.versereach.email.domain.repository.EmailRepository


object EmailModule {
    val emailRepository: EmailRepository by lazy {
        EmailRepositoryImpl()
    }
}