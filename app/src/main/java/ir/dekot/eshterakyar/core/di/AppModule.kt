package ir.dekot.eshterakyar.core.di

import ir.dekot.eshterakyar.core.database.GetUserUseCase
import ir.dekot.eshterakyar.core.domain.usecase.GetPreferredCurrencyUseCase
import ir.dekot.eshterakyar.core.domain.usecase.SetPreferredCurrencyUseCase
import ir.dekot.eshterakyar.core.domain.usecase.UpdateUserUseCase
import ir.dekot.eshterakyar.core.preferences.CurrencyPreferences
import ir.dekot.eshterakyar.core.themePreferences.ThemeViewModel
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.CancelReminderUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.DeleteSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetActiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetAllSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetCategoryBreakdownUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetInactiveSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetNearingRenewalSubscriptionsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionByIdUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionStatsUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.GetSubscriptionsSortedByCreationUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.InsertSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.ScheduleReminderUseCase
import ir.dekot.eshterakyar.feature_addSubscription.domain.usecase.UpdateSubscriptionUseCase
import ir.dekot.eshterakyar.feature_addSubscription.presentation.viewmodel.AddSubscriptionViewModel
import ir.dekot.eshterakyar.feature_home.domain.usecase.GetUserGreetingUseCase
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.EditSubscriptionViewModel
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.HomeViewModel
import ir.dekot.eshterakyar.feature_home.presentation.viewmodel.SubscriptionDetailViewModel
import ir.dekot.eshterakyar.screens.ProfileDetailViewModel
import ir.dekot.eshterakyar.screens.ProfileViewModel
import ir.dekot.eshterakyar.screens.ReportsViewModel
import ir.dekot.eshterakyar.screens.SettingsViewModel
import ir.dekot.eshterakyar.screens.personalInfo.viewmodel.PersonalInformationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // ViewModels
    viewModelOf(constructor = ::ThemeViewModel)
    viewModelOf(constructor = ::HomeViewModel)
    viewModelOf(constructor = ::SubscriptionDetailViewModel)
    viewModelOf(constructor = ::EditSubscriptionViewModel)
    viewModelOf(constructor = ::AddSubscriptionViewModel)
    viewModelOf(constructor = ::ReportsViewModel)
    viewModelOf(constructor = ::ProfileViewModel)
    viewModelOf(constructor = ::ProfileDetailViewModel)
    viewModelOf(constructor = ::PersonalInformationViewModel)
    viewModelOf(constructor = ::SettingsViewModel)

    // Preferences
    single { CurrencyPreferences(androidContext()) }

    // Use Cases
    singleOf(constructor = ::GetAllSubscriptionsUseCase)
    singleOf(constructor = ::GetSubscriptionsSortedByCreationUseCase)
    singleOf(constructor = ::GetActiveSubscriptionsUseCase)
    singleOf(constructor = ::GetInactiveSubscriptionsUseCase)
    singleOf(constructor = ::GetNearingRenewalSubscriptionsUseCase)
    singleOf(constructor = ::GetSubscriptionStatsUseCase)
    singleOf(constructor = ::GetCategoryBreakdownUseCase)
    singleOf(constructor = ::InsertSubscriptionUseCase)
    singleOf(constructor = ::GetSubscriptionByIdUseCase)
    singleOf(constructor = ::UpdateSubscriptionUseCase)
    singleOf(constructor = ::DeleteSubscriptionUseCase)
    singleOf(constructor = ::GetUserGreetingUseCase)

    // User related
    singleOf(constructor = ::GetUserUseCase)
    singleOf(constructor = ::UpdateUserUseCase)

    // Currency related
    singleOf(constructor = ::GetPreferredCurrencyUseCase)
    singleOf(constructor = ::SetPreferredCurrencyUseCase)

    // WorkManager & Reminder related
    single { androidx.work.WorkManager.getInstance(androidContext()) }
    singleOf(constructor = ::ScheduleReminderUseCase)
    singleOf(constructor = ::CancelReminderUseCase)
}
