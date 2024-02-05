package com.oguzdogdu.walliescompose.data.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val walliesDispatcher: WalliesDispatchers)

enum class WalliesDispatchers {
    Default,
    IO,
}
