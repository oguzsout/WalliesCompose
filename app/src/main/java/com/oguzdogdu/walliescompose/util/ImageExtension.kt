package com.oguzdogdu.walliescompose.util

fun <T> resolveImage(
    profileImage: T?,
    uri: T?,
    defaultImage: T
): T {
    return when {
        profileImage == null && uri == null -> defaultImage
        profileImage != null && uri == null -> profileImage
        uri != null -> uri
        else -> defaultImage
    }
}