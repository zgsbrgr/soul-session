package com.zgsbrgr.soulsession.core.common.result

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.lang.Exception
import org.junit.Assert.assertEquals

class ResultTest {

    @Test
    fun result_catches_error() = runTest {
        flow {
            emit(1)
            throw Exception("Test Done")
        }
            .asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(1), awaitItem())

                when(val error = awaitItem()) {
                    is Result.Error -> assertEquals(
                        "Test Done",
                        error.exception?.message
                    )
                    Result.Loading,
                    is Result.Success -> throw IllegalStateException(
                        "the flow should have emitted an error as result"
                    )
                }
                awaitComplete()
            }

    }

}