package com.oguzdogdu.walliescompose.features.detail

sealed class DetailScreenEvent {
    data object GetPhotoDetails : DetailScreenEvent()
}
