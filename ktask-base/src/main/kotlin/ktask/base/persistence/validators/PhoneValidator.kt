/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.persistence.validators

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import ktask.base.env.Tracer

/**
 * Verifies if a phone number is in the correct format.
 *
 * @see IValidator
 * @see ValidationException
 */
public object PhoneValidator : IValidator<String> {
    private val tracer = Tracer<PhoneValidator>()

    /** The maximum length of a phone number. */
    private const val MAX_PHONE_LENGTH: Int = 15

    public override fun check(value: String): Result<String> {
        return runCatching {
            // Check for the maximum length of the phone number.
            if (value.length > MAX_PHONE_LENGTH) {
                throw ValidationException(
                    "Phone-number exceeds the maximum length of $MAX_PHONE_LENGTH characters: $value"
                )
            }

            val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

            // Region code is null for international numbers.
            val numberProto: Phonenumber.PhoneNumber = phoneUtil.parse(value, null)

            if (!phoneUtil.isValidNumber(numberProto)) {
                throw ValidationException("Invalid phone number: $value")
            }

            return@runCatching Result.success(value)
        }.getOrElse { error ->
            when (error) {
                is NumberParseException -> {
                    tracer.error(message = "Error parsing phone number: $value", cause = error)
                    return@getOrElse Result.failure(ValidationException("Error parsing phone number: $value. ${error.message}"))
                }

                is IllegalArgumentException -> {
                    return@getOrElse Result.failure(error)
                }

                else -> {
                    return@getOrElse Result.failure(ValidationException("${error.message}"))
                }
            }
        }
    }
}
