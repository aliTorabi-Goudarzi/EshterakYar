package ir.dekot.eshterakyar.feature_home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.feature_home.presentation.components.GreetingCard

@Composable
fun HomeScreen() {
    // توضیح: وضعیت باز/بسته بودن کارت خوش‌آمدگویی
    var isCardExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(140.dp))
        }

        // GreetingCard که روی محتوا قرار می‌گیرد
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            GreetingCard(
                modifier = Modifier.fillMaxWidth(),
                onExpandedChange = { expanded -> isCardExpanded = expanded }
            )
        }
    }
}
