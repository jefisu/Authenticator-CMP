package com.jefisu.authenticator.util

import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import com.varabyte.truthish.assertThat

fun assertSuccessResult(
    result: Result<Any, Error>,
    expectedData: Any
) {
    assertThat(result).isEqualTo(Result.Success(expectedData))
}

fun assertErrorResult(
    result: Result<Any, Error>,
    expectedError: Error
) {
    assertThat(result).isEqualTo(Result.Error(expectedError))
}
