package com.oguzdogdu.walliescompose.features.latest

sealed class LatestScreenEvent {
    data object FetchLatestData : LatestScreenEvent()
}
