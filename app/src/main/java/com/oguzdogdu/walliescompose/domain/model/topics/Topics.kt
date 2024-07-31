package com.oguzdogdu.walliescompose.domain.model.topics

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Topics(val id: String?, val title: String?, val titleBackground: String?)
