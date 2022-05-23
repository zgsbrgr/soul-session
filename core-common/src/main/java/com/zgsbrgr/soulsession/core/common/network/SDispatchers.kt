package com.zgsbrgr.soulsession.core.common.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher( val sDispatcher: SDispatchers )

enum class SDispatchers {
    IO
}