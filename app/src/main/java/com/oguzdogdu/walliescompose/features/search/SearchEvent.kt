package com.oguzdogdu.walliescompose.features.search

sealed class SearchEvent {
    data class EnteredSearchQuery(
        val query: String?,
        val language: String?,
    ) : SearchEvent()
    data object GetAppLanguageValue : SearchEvent()
    data class OpenSpeechDialog(val isOpen: Boolean) : SearchEvent()
}
