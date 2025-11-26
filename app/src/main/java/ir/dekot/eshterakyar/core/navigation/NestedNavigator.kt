package ir.dekot.eshterakyar.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class NestedNavigator (startDestination: Any) {
    val backStack: SnapshotStateList<Any> = mutableStateListOf(startDestination)

    val currentDestination: Any?
        get() = backStack.lastOrNull()

    fun navigateTo(destination: Any) {
        backStack.add(destination)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}