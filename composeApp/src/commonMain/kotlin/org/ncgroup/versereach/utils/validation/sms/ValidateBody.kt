package validation.sms

import org.ncgroup.versereach.utils.validation.ValidationResult

class ValidateBody {
    operator fun invoke(body: String): ValidationResult {
        if (body.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Sms body is required."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}