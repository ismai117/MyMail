package org.ncgroup.versereach.utils.validation


data class ValidationResult(
    val successful: Boolean = false,
    val errorMessage: String = ""
)