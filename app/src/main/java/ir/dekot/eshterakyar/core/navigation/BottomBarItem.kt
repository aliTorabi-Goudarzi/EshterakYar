package ir.dekot.eshterakyar.core.navigation

import androidx.compose.runtime.saveable.Saver
import androidx.navigation3.runtime.NavKey
import ir.dekot.eshterakyar.R
import kotlinx.serialization.Serializable


val bottomBarList: List<BottomBarItem> = listOf(
    BottomBarItem.Home,
    BottomBarItem.AddSubscription,
    BottomBarItem.Reports,
    BottomBarItem.Profile,
)

@Serializable
sealed class BottomBarItem(val icon : Int){
    @Serializable
    data object Home : BottomBarItem(
        icon = R.drawable.ic_dashboard
    )
    @Serializable
    data object AddSubscription : BottomBarItem(
        icon = R.drawable.ic_add
    )
    @Serializable
    data object Reports : BottomBarItem(
        icon = R.drawable.ic_report
    )
    @Serializable
    data object Profile : BottomBarItem(
        icon = R.drawable.ic_profile
    )

    companion object {
        val bottomBarStateSaver = Saver<BottomBarItem,String>(
            save = {it::class.simpleName ?: "Unknown"},
            restore = {
                when(it){
                    Profile::class.simpleName -> Profile
                    Reports::class.simpleName -> Reports
                    AddSubscription::class.simpleName -> AddSubscription
                    Home::class.simpleName -> Home
                    else -> Home
                }
            }
        )
    }
}

