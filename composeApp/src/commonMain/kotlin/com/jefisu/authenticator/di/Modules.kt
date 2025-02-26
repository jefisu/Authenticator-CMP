package com.jefisu.authenticator.di

import com.jefisu.authenticator.data.AccountRepositoryImpl
import com.jefisu.authenticator.data.TotpServiceImpl
import com.jefisu.authenticator.data.database.getAccountDatabase
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.service.TotpService
import com.jefisu.authenticator.domain.usecase.AddAccountUseCase
import com.jefisu.authenticator.domain.usecase.DeleteAccountUseCase
import com.jefisu.authenticator.domain.usecase.GenerateTotpUseCase
import com.jefisu.authenticator.domain.usecase.GetAllAccountsUseCase
import com.jefisu.authenticator.domain.usecase.SearchAccountsUseCase
import com.jefisu.authenticator.domain.usecase.UseCases
import com.jefisu.authenticator.platform.TotpQrScanner
import com.jefisu.authenticator.platform.TotpQrScannerImpl
import com.jefisu.authenticator.presentation.addkeymanually.AddKeyManuallyViewModel
import com.jefisu.authenticator.presentation.qrscanner.QrScannerViewModel
import com.jefisu.authenticator.presentation.totp.TotpViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::TotpQrScannerImpl).bind<TotpQrScanner>()
    singleOf(::getAccountDatabase)
    singleOf(::AccountRepositoryImpl).bind<AccountRepository>()
    singleOf(::TotpServiceImpl).bind<TotpService>()

    singleOf(::AddAccountUseCase)
    singleOf(::GetAllAccountsUseCase)
    singleOf(::GenerateTotpUseCase)
    singleOf(::DeleteAccountUseCase)
    singleOf(::SearchAccountsUseCase)
    singleOf(::UseCases)

    viewModelOf(::TotpViewModel)
    viewModelOf(::QrScannerViewModel)
    viewModelOf(::AddKeyManuallyViewModel)
}
