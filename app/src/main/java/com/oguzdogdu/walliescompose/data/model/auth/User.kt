package com.oguzdogdu.walliescompose.data.model.auth

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val name: String?,
    val surname: String?,
    val email: String?,
    val image: String?,
    val favorites: List<HashMap<String,String>>?
)

fun User.toUserDomain() =
    com.oguzdogdu.walliescompose.domain.model.auth.User(
        name = name,
        surname = surname,
        email = email,
        image = image,
        favorites = favorites
    )