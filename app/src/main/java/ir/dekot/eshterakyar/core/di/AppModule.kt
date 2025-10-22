package ir.dekot.eshterakyar.core.di

import android.app.Application
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetAllSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetActiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetInactiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetNearingRenewalSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionStatsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.InsertSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionByIdUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.UpdateSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.DeleteSubscriptionUseCase
import ir.dekot.eshterakyar.core.themePreferences.ThemeViewModel
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.HomeViewModel
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.SubscriptionDetailViewModel
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.EditSubscriptionViewModel
import ir.dekot.eshterakyar.feature_home.domain.usecase.GetUserGreetingUseCase
import ir.dekot.eshterakyar.screens.ReportsViewModel
import ir.dekot.eshterakyar.feature_addSubscription.presentation.viewmodel.AddSubscriptionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    
    // ViewModels
    viewModel { ThemeViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { SubscriptionDetailViewModel(get(), get()) }
    viewModel { EditSubscriptionViewModel(get(), get()) }
    viewModel { AddSubscriptionViewModel(get()) }
    viewModel { ReportsViewModel(get()) }
    
    // Use Cases
    single { GetAllSubscriptionsUseCase(get()) }
    single { GetActiveSubscriptionsUseCase(get()) }
    single { GetInactiveSubscriptionsUseCase(get()) }
    single { GetNearingRenewalSubscriptionsUseCase(get()) }
    single { GetSubscriptionStatsUseCase(get()) }
    single { InsertSubscriptionUseCase(get()) }
    single { GetSubscriptionByIdUseCase(get()) }
    single { UpdateSubscriptionUseCase(get()) }
    single { DeleteSubscriptionUseCase(get()) }
    single { GetUserGreetingUseCase() }
}