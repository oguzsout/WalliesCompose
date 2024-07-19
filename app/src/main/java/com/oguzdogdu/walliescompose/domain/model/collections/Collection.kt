package com.oguzdogdu.walliescompose.domain.model.collections

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.data.model.maindto.User
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Collection(
    val id: String?,
    val title: String?,
    val desc: String?,
    val user: User?
)
