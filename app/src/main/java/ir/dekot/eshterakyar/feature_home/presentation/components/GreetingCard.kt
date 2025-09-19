package ir.dekot.eshterakyar.feature_home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.dekot.eshterakyar.R
import sv.lib.squircleshape.CornerSmoothing
import sv.lib.squircleshape.SquircleShape

@Composable
fun GreetingCard(
    modifier: Modifier = Modifier,
    onExpandedChange: (Boolean) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)

    ) {
        // کارت اصلی با سایه برای جلوه بهتر
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = SquircleShape(
                        topStart = 0,
                        topEnd = 0,
                        bottomStart = 35,
                        bottomEnd = 35,
                        smoothing = CornerSmoothing.High
                    )
                )
            ,
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF343a40),
            ),
            shape = SquircleShape(
                topStart = 0,
                topEnd = 0,
                bottomStart = 35,
                bottomEnd = 35,
                smoothing = CornerSmoothing.High
            ),
        ) {
            // بقیه کد کارت همانطور که بود...
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
            ) {
                // محتوای اصلی کارت
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 40.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 30.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // محتوای ثابت
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = "profileImage",
                            modifier = Modifier
                                .clip(
                                    SquircleShape(
                                        30,
                                        smoothing = CornerSmoothing.Medium
                                    )
                                )
                                .size(60.dp)
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "سلام وقتت بخیر👋",
                                fontSize = 20.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color.White,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    textDirection = TextDirection.ContentOrRtl
                                )
                            )
                            Text(
                                text = "علی ترابی گودرزی",
                                fontSize = 16.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color.White,
                                fontWeight = FontWeight.Medium,
                                style = TextStyle(
                                    textDirection = TextDirection.ContentOrRtl
                                ),
                            )
                        }
                    }

                    // محتوای بازشونده
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutLinearInEasing
                            )
                        )
                    ) {
                        // کد Row با کارت‌های سه‌گانه...
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // همان کد قبلی کارت‌های سه‌گانه
                            Card(
                                shape = SquircleShape(
                                    topStart = 30,
                                    topEnd = 10,
                                    bottomStart = 30,
                                    bottomEnd = 10,
                                    smoothing = CornerSmoothing.High
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) { }
                                            .padding(vertical = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Healing,
                                            contentDescription = "label",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "label",
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(3.dp))
                            Card(
                                shape = SquircleShape(
                                    topStart = 10,
                                    topEnd = 10,
                                    bottomStart = 10,
                                    bottomEnd = 10,
                                    smoothing = CornerSmoothing.High
                                ),
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black
                                ),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) { }
                                            .padding(vertical = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Wallet,
                                            contentDescription = "label",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "label",
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(3.dp))
                            Card(
                                shape = SquircleShape(
                                    topStart = 10,
                                    topEnd = 30,
                                    bottomStart = 10,
                                    bottomEnd = 30,
                                    smoothing = CornerSmoothing.High
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black
                                ),
                                modifier = Modifier.weight(1f),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {}
                                            .padding(vertical = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.TripOrigin,
                                            contentDescription = "label",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "label",
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // دکمه آویزان
        Canvas(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 0.dp)
                .size(width = 40.dp, height = 20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isExpanded = !isExpanded
                    onExpandedChange(isExpanded)
                }
        ) {
//            val path = Path().apply {
//                moveTo(0f, 0f)
//                lineTo(size.width, 0f)
//                arcTo(
//                    rect = Rect(
//                        left = 0f,
//                        top = -size.height,
//                        right = size.width,
//                        bottom = size.height
//                    ),
//                    startAngleDegrees = 0f,
//                    sweepAngleDegrees = 180f,
//                    forceMoveTo = false
//                )
//                close()
//            }
//            drawPath(
//                path = path,
//                color = Color(0xFFB0B3B8)
//            )
        }

        // آیکون روی دکمه
        Icon(
            imageVector = if (isExpanded)
                Icons.Default.KeyboardArrowUp
            else
                Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-4).dp)
                .size(20.dp),
            tint = if (isSystemInDarkTheme()) Color.Black else Color.White
        )
    }
}


