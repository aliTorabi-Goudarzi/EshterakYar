package ir.dekot.eshterakyar.core.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ir.dekot.eshterakyar.core.themePreferences.ThemeViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun EshterakYarApp() {
    val navController = rememberNavController()
    val viewModel: ThemeViewModel = viewModel()
    val isDark by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),  // کل صفحه رو پر کن
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.White,
            bottomBar = {}
        ) { innerPadding ->
            NavHostScreen(
                navHostController = navController,
                padding = innerPadding
            )
        }
        // Bottom Bar شناور بالای محتوا
        AnimatedNavigationBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter),
            barColor = if (isDark) Color.Black.copy(alpha = 0.7f) else Color(0xFF343a40),
            circleColor = if (isDark) Color.White.copy(alpha = 0.2f) else  Color(0xFFB0B3B8),
            selectedColor = if (isDark) Color.White else Color.White,
            unselectedColor = if (isDark) Color.White.copy(alpha = 0.7f) else Color.White
            )
    }
}