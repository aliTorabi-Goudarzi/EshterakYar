package ir.dekot.eshterakyar.feature_privacy.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.utils.LocalTheme
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

/** صفحه حریم خصوصی */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(rootNavigator: RootNavigator) {
    val theme = LocalTheme.current

    Scaffold(
            containerColor = theme.backgroundColor,
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    text = "حریم خصوصی",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = theme.onSurface
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { rootNavigator.goBack() }) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "بازگشت",
                                        tint = theme.onSurface
                                )
                            }
                        },
                        colors =
                                TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color.Transparent
                                )
                )
            }
    ) { padding ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(padding)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Local First Card
            PolicyCard(
                    title = "🔒 ذخیره‌سازی محلی",
                    content =
                            "تمام اطلاعات شما فقط روی دستگاه خودتان ذخیره می‌شود. هیچ داده‌ای به سرور ارسال نمی‌شود."
            )

            // No Account Card
            PolicyCard(
                    title = "👤 بدون ثبت‌نام",
                    content =
                            "اشتراک‌یار نیازی به ثبت‌نام یا ایجاد حساب کاربری ندارد. شما می‌توانید بدون ارائه اطلاعات شخصی از اپ استفاده کنید."
            )

            // Data Control Card
            PolicyCard(
                    title = "🎮 کنترل کامل داده‌ها",
                    content =
                            "شما می‌توانید هر زمان که بخواهید اطلاعات خود را ویرایش یا حذف کنید. با حذف اپ، تمام داده‌ها به‌طور کامل از دستگاه پاک می‌شوند."
            )

            // No Ads Card
            PolicyCard(
                    title = "🚫 بدون تبلیغات",
                    content =
                            "اشتراک‌یار هیچ تبلیغاتی ندارد و هیچ اطلاعاتی برای اهداف تبلیغاتی جمع‌آوری نمی‌کند."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                    text = "نسخه ۱.۰.۰ • توسعه داده شده با ❤️",
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PolicyCard(title: String, content: String) {
    val theme = LocalTheme.current

    Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = theme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = SquircleShape(radius = 16.dp, smoothing = CornerSmoothing.Medium)
    ) {
        Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = theme.onSurface
            )

            Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.onSurfaceVariant
            )
        }
    }
}
