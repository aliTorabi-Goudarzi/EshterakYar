package ir.dekot.eshterakyar.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screens: NavKey {
    @Serializable
    data object NestedGraph : Screens() // این صفحه ورودی ندارد، پس object باقی می‌ماند

    @Serializable
    data class SubscriptionDetail(val subscriptionId: Long) : Screens()

    @Serializable
    data class EditSubscription(val subscriptionId: Long) : Screens()

}
