package com.oguzdogdu.walliescompose.util

import java.util.regex.Pattern

object FieldValidators {

    fun isValidEmail(email: String?): Boolean {
        val regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
        return regex.matches(email.orEmpty())
    }

    fun isStringContainNumber(text: String?): Boolean {
        val pattern = Pattern.compile(".*\\d.*")
        val matcher = pattern.matcher(text.toString())
        return matcher.matches()
    }

    fun isStringLowerAndUpperCase(text: String?): Boolean {
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

    fun isStringContainSpecialCharacter(text: String?): Boolean {
        val specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9 ]")
        val specialCharacterMatcher = specialCharacterPattern.matcher(text.orEmpty())
        return specialCharacterMatcher.find()
    }
}
