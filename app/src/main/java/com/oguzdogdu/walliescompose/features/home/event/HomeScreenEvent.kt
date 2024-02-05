package com.oguzdogdu.walliescompose.features.home.event

sealed interface HomeScreenEvent {
    data object FetchMainScreenUserData : HomeScreenEvent
    data object FetchHomeScreenLists: HomeScreenEvent
}