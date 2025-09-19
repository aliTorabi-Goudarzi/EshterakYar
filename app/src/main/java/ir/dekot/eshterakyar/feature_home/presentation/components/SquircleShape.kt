package ir.dekot.eshterakyar.feature_home.presentation.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow

class AdjustableSquircleShape(
    private val curvature: Double = 3.0 // هر چه مقدار بالاتر → شکل مربع‌تر
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val r = min(size.width, size.height) / 2
        val rPow = r.toDouble().pow(curvature)

        val path = Path().apply {
            moveTo(-r, 0f)

            for (x in -r.toInt()..r.toInt()) {
                val y = (rPow - abs(x.toDouble().pow(curvature)))
                    .pow(1.0 / curvature)
                    .toFloat()
                lineTo(x.toFloat(), y)
            }

            for (x in r.toInt() downTo -r.toInt()) {
                val y = -(rPow - abs(x.toDouble().pow(curvature)))
                    .pow(1.0 / curvature)
                    .toFloat()
                lineTo(x.toFloat(), y)
            }

            close()
            translate(Offset(size.width / 2, size.height / 2))
        }

        return Outline.Generic(path)
    }
}





class AdjustableRoundedRectShape(
    private val curvature: Double = 4.0 // 2.0 = بیضی کامل، مقدار بیشتر → گوشه تیزتر
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val a = size.width / 2   // نیمه عرض
        val b = size.height / 2  // نیمه ارتفاع

        val path = Path().apply {
            moveTo(-a, 0f)

            // نیمه بالایی
            for (x in -a.toInt()..a.toInt()) {
                val dx = abs(x.toDouble()) / a
                val y = (1 - dx.pow(curvature))
                    .pow(1.0 / curvature) * b
                lineTo(x.toFloat(), y.toFloat())
            }

            // نیمه پایینی
            for (x in a.toInt() downTo -a.toInt()) {
                val dx = abs(x.toDouble()) / a
                val y = -(1 - dx.pow(curvature))
                    .pow(1.0 / curvature) * b
                lineTo(x.toFloat(), y.toFloat())
            }

            close()
            translate(Offset(size.width / 2, size.height / 2))
        }

        return Outline.Generic(path)
    }
}

//این ها اشکال هندسی استفاده در برنامه هستن و ورودی های مناسب آن ها
//SquircleShape(percent = 30,smoothing = CornerSmoothing.High , Medium , Full)
//AdjustableSquircleShape()
//AdjustableRoundedRectShape(4.0)
//AdjustableRoundedRectShape(4.5)
//AdjustableRoundedRectShape(5.0)