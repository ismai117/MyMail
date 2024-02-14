package com.ncgroup.versereach.utils.validation



class ValidatePrompt {
    operator fun invoke(prompt: String): ValidationResult {
        if (prompt.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "A Prompt is required."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}