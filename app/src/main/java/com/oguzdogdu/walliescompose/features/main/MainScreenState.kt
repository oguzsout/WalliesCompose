package com.oguzdogdu.walliescompose.features.main

import androidx.compose.runtime.Stable

@Stable
data class MainScreenState(val themeValues: String? = null,val userAuth:Boolean=false)
