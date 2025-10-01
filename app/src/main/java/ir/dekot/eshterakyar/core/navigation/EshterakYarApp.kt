package ir.dekot.eshterakyar.core.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ir.dekot.eshterakyar.core.utils.LocalTheme

@SuppressLint("SuspiciousIndentation")
@Composable
fun EshterakYarApp() {
    val theme = LocalTheme.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background( theme.backgroundColor)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),  // کل صفحه رو پر کن
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
            bottomBar = {}
        ) { innerPadding ->
            NestedGraph(
                padding = innerPadding
            )
        }
    }
}