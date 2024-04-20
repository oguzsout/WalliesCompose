package com.oguzdogdu.walliescompose.util

import com.oguzdogdu.walliescompose.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.regex.Pattern

object FieldValidators {

    fun isValidEmail(email: String): Boolean {
        val regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
        return regex.matches(email)
    }

    fun isValidPasswordCheck(input: String): Boolean {
        return when {
            input.length < 6 -> false
            !isStringContainNumber(input) -> false
            !isStringLowerAndUpperCase(input) -> false
            !isStringContainSpecialCharacter(input) -> false
            else -> true
        }
    }

    fun isValidPasswordChecksStatus(input: String): Int {
        return when {
            input.length < 6 -> R.string.password_length
            !isStringContainNumber(input) -> R.string.required_at_least_1_digit
            !isStringLowerAndUpperCase(input) -> R.string.password_must_contain_upper_and_lower_case_letters
            !isStringContainSpecialCharacter(input) -> R.string.one_special_character_required
            else -> 0
        }
    }

    fun isValidEmailFlow(email: String): Flow<Boolean> = flow {
        val regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
        emit(regex.matches(email))
    }

    fun isValidPasswordCheckFlow(input: String): Flow<Boolean> = flow {
        val isValid = input.length >= 6 &&
                isStringContainNumber(input) &&
                isStringLowerAndUpperCase(input) &&
                isStringContainSpecialCharacter(input)
        emit(isValid)
    }

    fun isValidPasswordCheckRuleSetFlow(input: String): Flow<List<Pair<String, Boolean>>> = flow {
        if (input.isBlank()) {
            val defaultValidations = listOf(
                "Min 6 characters" to false,
                "Contains number" to false,
                "Contains lower and upper case" to false,
                "Contains special character" to false
            )
            emit(defaultValidations)
        } else {
            val lengthValid = input.length >= 6
            val containsNumber = isStringContainNumber(input)
            val containsLowerAndUpperCase = isStringLowerAndUpperCase(input)
            val containsSpecialCharacter = isStringContainSpecialCharacter(input)

            val passwordValidations = listOf(
                "Min 6 characters" to lengthValid,
                "Contains number" to containsNumber,
                "Contains lower and upper case" to containsLowerAndUpperCase,
                "Contains special character" to containsSpecialCharacter
            )

            emit(passwordValidations)
        }
    }


    private fun isStringContainNumber(text: String?): Boolean {
        val pattern = Pattern.compile(".*\\d.*")
        val matcher = pattern.matcher(text.toString())
        return matcher.matches()
    }

    private fun isStringLowerAndUpperCase(text: String?): Boolean {
        val lowerCasePattern = Pattern.compile(".*[a-z].*")
        val upperCasePattern = Pattern.compile(".*[A-Z].*")
        val lowerCasePatterMatcher = lowerCasePattern.matcher(text.toString())
        val upperCasePatterMatcher = upperCasePattern.matcher(text.toString())
        return if (!lowerCasePatterMatcher.matches()) {
            false
        } else {
            upperCasePatterMatcher.matches()
        }
    }

    private fun isStringContainSpecialCharacter(text: String?): Boolean {
        val specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9 ]")
        val specialCharacterMatcher = specialCharacterPattern.matcher(text.orEmpty())
        return specialCharacterMatcher.find()
    }
}
