package ir.dekot.eshterakyar.feature_support.presentation.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.dekot.eshterakyar.core.navigation.RootNavigator
import ir.dekot.eshterakyar.core.utils.LocalTheme
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

/** صفحه پشتیبانی و ارتباط با ما */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(rootNavigator: RootNavigator) {
    val theme = LocalTheme.current
    val context = LocalContext.current

    Scaffold(
            containerColor = theme.backgroundColor,
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    text = "پشتیبانی",
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
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                    text = "راه‌های ارتباط با ما",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = theme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email Card
            ContactCard(
                    icon = Icons.Default.Email,
                    title = "ایمیل",
                    subtitle = "support@eshterakyar.ir",
                    iconColor = Color(0xFF2196F3),
                    onClick = {
                        val intent =
                                Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:support@eshterakyar.ir")
                                    putExtra(Intent.EXTRA_SUBJECT, "پشتیبانی اشتراک‌یار")
                                }
                        context.startActivity(intent)
                    }
            )

            // Telegram Card
            ContactCard(
                    icon = Icons.Default.Phone,
                    title = "تلگرام",
                    subtitle = "@EshterakYarSupport",
                    iconColor = Color(0xFF0088CC),
                    onClick = {
                        val intent =
                                Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://t.me/EshterakYarSupport")
                                }
                        context.startActivity(intent)
                    }
            )

            Spacer(modifier = Modifier.weight(1f))

            // App Version
            Text(
                    text = "نسخه ۱.۰.۰",
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun ContactCard(
        icon: ImageVector,
        title: String,
        subtitle: String,
        iconColor: Color,
        onClick: () -> Unit
) {
    val theme = LocalTheme.current

    Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = theme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = SquircleShape(radius = 16.dp, smoothing = CornerSmoothing.Medium)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = theme.onSurface
                )

                Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.onSurfaceVariant
                )
            }
        }
    }
}
