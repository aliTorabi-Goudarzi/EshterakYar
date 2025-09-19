package ir.dekot.eshterakyar.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Person
import ir.dekot.eshterakyar.R

sealed class Screens(
    val route: String,
//    val title: String,
    val icon: Int
) {
    object Home : Screens(
        route = "home",
//        title = "خانه",
        icon = R.drawable.ic_dashboard
    )

    object AddSubscription : Screens(
        route = "add_subscription",
//        title = "افزودن",
        icon = R.drawable.ic_add
    )

    object Reports : Screens(
        route = "reports",
//        title = "گزارش‌ها",
        icon = R.drawable.ic_report
    )

    object Profile : Screens(
        route = "profile",
//        title = "پروفایل",
        icon = R.drawable.ic_profile
    )

    companion object {
        val screenList: List<Screens> = listOf(Profile, Reports, AddSubscription, Home)
    }

    fun routWithArgs(vararg args: String) =
        buildString {
            append(route)
            args.forEachIndexed { index, _ ->
                append("?$index={$index}")
            }
        }

    fun paramWithArgs(vararg args: String) =
        buildString {
            append(route)
            args.forEachIndexed { index, value ->
                append("?$index=$value")
            }
        }


}
