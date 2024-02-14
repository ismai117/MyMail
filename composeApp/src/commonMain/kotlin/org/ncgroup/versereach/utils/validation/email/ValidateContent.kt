package org.ncgroup.versereach.utils.validation.email

import org.ncgroup.versereach.utils.validation.ValidationResult

class ValidateContent {
    operator fun invoke(content: String): ValidationResult {
        if (content.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Email Content is required."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}