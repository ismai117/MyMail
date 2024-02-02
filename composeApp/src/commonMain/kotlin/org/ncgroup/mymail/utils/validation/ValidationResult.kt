package org.ncgroup.mymail.utils.validation


data class ValidationResult(
    val successful: Boolean = false,
    val errorMessage: String = ""
)