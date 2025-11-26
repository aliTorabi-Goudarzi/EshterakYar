package ir.dekot.eshterakyar.core.di

import ir.dekot.eshterakyar.feature_addSubscription.data.repository.SubscriptionRepositoryImpl
import ir.dekot.eshterakyar.feature_addSubscription.domain.repository.SubscriptionRepository
import ir.dekot.eshterakyar.core.database.UserRepository
import ir.dekot.eshterakyar.core.database.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    // Provide SubscriptionRepository
    single<SubscriptionRepository> {
        SubscriptionRepositoryImpl(get())
    }

    // Provide UserRepository
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
}