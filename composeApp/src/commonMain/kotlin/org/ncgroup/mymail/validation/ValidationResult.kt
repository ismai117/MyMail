package org.ncgroup.mymail.validation


data class ValidationResult(
    val successful: Boolean = false,
    val errorMessage: String = ""
)