package com.oguzdogdu.walliescompose.features.popular

sealed class PopularScreenEvent {
    data object FetchPopularData : PopularScreenEvent()
}
