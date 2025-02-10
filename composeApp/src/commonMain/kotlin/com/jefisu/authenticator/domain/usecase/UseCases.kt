package com.jefisu.authenticator.domain.usecase

data class UseCases(
    val addAccount: AddAccountUseCase,
    val generateTotp: GenerateTotpUseCase,
    val getAllAccounts: GetAllAccountsUseCase
)
