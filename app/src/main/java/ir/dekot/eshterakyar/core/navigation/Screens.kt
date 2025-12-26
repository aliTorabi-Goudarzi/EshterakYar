package ir.dekot.eshterakyar.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens {
    @Serializable
    data object NestedGraph : Screens() // این صفحه ورودی ندارد، پس object باقی می‌ماند

    @Serializable data class SubscriptionDetail(val subscriptionId: Long) : Screens()

    @Serializable data class EditSubscription(val subscriptionId: Long) : Screens()

    @Serializable data object SettingsScreen : Screens()

    @Serializable data object ProfileDetailScreen : Screens()

    @Serializable data object PersonalInformation : Screens()

    @Serializable data object CategoryManagement : Screens()

    @Serializable data object Onboarding : Screens()

    @Serializable data object Support : Screens()

    @Serializable data object PrivacyPolicy : Screens()
}
