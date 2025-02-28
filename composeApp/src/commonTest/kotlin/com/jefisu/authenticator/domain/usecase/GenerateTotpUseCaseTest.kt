package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.data.FakeTotpService
import com.jefisu.authenticator.util.TestUtil
import com.varabyte.truthish.assertThat
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GenerateTotpUseCaseTest {

    private lateinit var generateTotpUseCase: GenerateTotpUseCase
    private lateinit var service: FakeTotpService

    @BeforeTest
    fun setup() {
        service = FakeTotpService()
        generateTotpUseCase = GenerateTotpUseCase(service)
    }

    @Test
    fun `execute with valid account returns TOTP code`() = runTest {
        val account = TestUtil.VALID_ACCOUNT
        service.setNextCode("123456")
        val result = generateTotpUseCase.execute(account)
        assertThat(result).isEqualTo("123456")
    }

    @Test
    fun `execute with valid account when service fails throws exception`() = runTest {
        val account = TestUtil.VALID_ACCOUNT
        service.setNextCallToFail()
        assertFailsWith<IllegalArgumentException> {
            generateTotpUseCase.execute(account)
        }
    }

    @Test
    fun `execute with account with invalid secret throws exception`() = runTest {
        val invalidAccount = TestUtil.VALID_ACCOUNT.copy(secret = "INVALID_SECRET")
        service.setNextCallToFail()
        assertFailsWith<IllegalArgumentException> {
            generateTotpUseCase.execute(invalidAccount)
        }
    }

    @Test
    fun `execute multiple times with same account returns same TOTP code when code is valid`() =
        runTest {
            val account = TestUtil.VALID_ACCOUNT
            val code = "345678"
            service.setNextCode(code)
            repeat(3) {
                val result = generateTotpUseCase.execute(account)
                assertThat(result).isEqualTo(code)
            }
        }
}