package com.jefisu.authenticator.di

import com.jefisu.authenticator.data.AccountRepositoryImpl
import com.jefisu.authenticator.data.TotpServiceImpl
import com.jefisu.authenticator.data.database.getAccountDatabase
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.service.TotpService
import com.jefisu.authenticator.domain.usecase.AddAccountUseCase
import com.jefisu.authenticator.domain.usecase.GenerateTotpUseCase
import com.jefisu.authenticator.domain.usecase.GetAllAccountsUseCase
import com.jefisu.authenticator.domain.usecase.UseCases
import com.jefisu.authenticator.presentation.addkeymanually.AddKeyManuallyViewModel
import com.jefisu.authenticator.presentation.qrscanner.QrScannerViewModel
import com.jefisu.authenticator.presentation.totp.TotpViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::getAccountDatabase)
    singleOf(::AccountRepositoryImpl).bind<AccountRepository>()
    singleOf(::TotpServiceImpl).bind<TotpService>()

    singleOf(::AddAccountUseCase)
    singleOf(::GetAllAccountsUseCase)
    singleOf(::GenerateTotpUseCase)
    singleOf(::UseCases)

    viewModelOf(::TotpViewModel)
    viewModelOf(::QrScannerViewModel)
    viewModelOf(::AddKeyManuallyViewModel)
}
