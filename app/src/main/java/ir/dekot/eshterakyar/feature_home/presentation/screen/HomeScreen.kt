package ir.dekot.eshterakyar.feature_home.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import ir.dekot.eshterakyar.feature_home.presentation.components.GreetingCard

@Composable
fun HomeScreen() {

    var isCardExpanded by remember { mutableStateOf(false) }
    var cardHeight by remember { mutableStateOf(0.dp) }
    var showDialogForIndex by remember { mutableStateOf<Int?>(null) }

    val density = LocalDensity.current

    val storyImageUrls = remember {
        listOf(
            "https://picsum.photos/id/10/200",
            "https://picsum.photos/id/11/200",
            "https://picsum.photos/id/12/200",
            "https://picsum.photos/id/13/200"
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(140.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                storyImageUrls.forEachIndexed { index, url ->
                    StoryItem(
                        imageUrl = url,
                        onClick = { showDialogForIndex = index }
                    )
                }
            }
        }

        if (showDialogForIndex != null) {
            val scale by animateFloatAsState(targetValue = if (showDialogForIndex != null) 1f else 0f)
            Dialog(onDismissRequest = { showDialogForIndex = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .padding(16.dp)
                        .scale(scale),
                    shape = MaterialTheme.shapes.large
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Dialog for item ${showDialogForIndex!! + 1}")
                    }
                }
            }
        }

        // GreetingCard که روی محتوا قرار می‌گیرد
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            GreetingCard(
                modifier = Modifier.fillMaxWidth(),
                onExpandedChange = { expanded ->
                    isCardExpanded = expanded
                }
            )
        }
    }
}

@Composable
fun StoryItem(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onClick: () -> Unit
) {
    val instagramColors = listOf(
        Color(0xFFFEDA75),
        Color(0xFFF58529),
        Color(0xFFDD2A7B),
        Color(0xFF8134AF),
        Color(0xFF515BD4)
    )
    Box(
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(colors = instagramColors),
                shape = CircleShape
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Story Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}
