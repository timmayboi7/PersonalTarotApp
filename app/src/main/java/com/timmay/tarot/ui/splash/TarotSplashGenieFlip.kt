package com.timmay.tarot.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timmay.tarot.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private val MaellenFamily = FontFamily(Font(R.font.maellen_e9j06))

@Composable
fun TarotGenieSplashFlip(
    modifier: Modifier = Modifier,
    title: String = "Personal Tarot Reader",
    cardResId: Int = R.mipmap.tarot_card,
    onFinished: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val rotation = remember { Animatable(0f) } // drives card spin
    val cardAlpha = remember { Animatable(1f) }
    val flash = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleScale = remember { Animatable(0.78f) }
    val borderAlpha = remember { Animatable(0f) }
    val sparkle = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scope.launch { rotation.animateTo(2.8f, tween(2400, easing = EaseInOutCubic)) }

        delay(1200)
        flash.snapTo(0f)
        scope.launch { flash.animateTo(1f, tween(140, easing = LinearEasing)) }
        scope.launch { cardAlpha.animateTo(0f, tween(420, easing = EaseOutCubic)) }
        delay(180)
        scope.launch { flash.animateTo(0f, tween(360, easing = EaseOutCubic)) }

        scope.launch { titleAlpha.animateTo(1f, tween(900, easing = EaseInOutCubic)) }
        scope.launch { titleScale.animateTo(1.08f, tween(900, easing = EaseOutCubic)) }
        scope.launch { borderAlpha.animateTo(1f, tween(1100, easing = EaseInOutCubic)) }
        scope.launch { sparkle.animateTo(1f, tween(1500, easing = EaseOutCubic)) }

        // Keep the mystical title on screen for a bit before moving on.
        delay(3200)
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF07060D), Color(0xFF0F0A1E))
                )
            )
    ) {
        MysticBackdrop(modifier = Modifier.fillMaxSize(), flash = flash.value)

        RotatingCardCluster(
            modifier = Modifier.fillMaxSize(),
            cardResId = cardResId,
            rotation = rotation.value,
            alpha = cardAlpha.value
        )

        FlashBurst(
            modifier = Modifier.fillMaxSize(),
            flash = flash.value
        )

        MysticTitle(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            title = title,
            alpha = titleAlpha.value,
            scale = titleScale.value,
            borderAlpha = borderAlpha.value,
            sparkle = sparkle.value,
            flash = flash.value
        )
    }
}

@Composable
private fun MysticBackdrop(
    modifier: Modifier,
    flash: Float
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF201437),
                    Color(0xFF0C0718)
                ),
                center = Offset(w * 0.45f, h * 0.48f),
                radius = min(w, h) * 0.85f
            )
        )

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF4A2D6F).copy(alpha = 0.55f),
                    Color.Transparent
                ),
                center = Offset(w * 0.56f, h * 0.62f),
                radius = min(w, h) * 0.65f
            ),
            blendMode = BlendMode.Screen
        )

        if (flash > 0f) {
            drawRect(Color.White.copy(alpha = 0.18f * flash), blendMode = BlendMode.Screen)
        }
    }
}

@Composable
private fun RotatingCardCluster(
    modifier: Modifier,
    cardResId: Int,
    rotation: Float,
    alpha: Float
) {
    val baseRotation = rotation * 360f
    Box(modifier = modifier) {
        val cards = listOf(
            Triple(-28f, -22f, -12f),
            Triple(16f, 10f, 9f),
            Triple(-6f, 34f, 28f)
        )
        cards.forEachIndexed { index, (ox, oy, tilt) ->
            val wobble = sin((rotation * 2f + index * 0.5f) * PI.toFloat()) * 6f
            val pulse = 1f + (cos(rotation * 3f + index) * 0.04f)
            Image(
                painter = painterResource(cardResId),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        translationX = ox * density
                        translationY = oy * density
                        rotationZ = baseRotation + tilt + wobble
                        scaleX = 0.94f * pulse
                        scaleY = 0.94f * pulse
                        this.alpha = alpha
                        shadowElevation = 18f
                        clip = true
                        shape = RoundedCornerShape(24.dp)
                    }
                    .blur((1.5f * (1f - alpha)).dp)
            )
        }
    }
}

@Composable
private fun FlashBurst(
    modifier: Modifier,
    flash: Float
) {
    if (flash <= 0f) return
    Canvas(modifier = modifier) {
        val radius = min(size.width, size.height) * (0.25f + 0.35f * flash)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.55f * flash), Color.Transparent),
                center = center,
                radius = radius
            ),
            radius = radius,
            center = center,
            blendMode = BlendMode.Screen
        )
    }
}

@Composable
private fun MysticTitle(
    modifier: Modifier,
    title: String,
    alpha: Float,
    scale: Float,
    borderAlpha: Float,
    sparkle: Float,
    flash: Float
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
                scaleX = scale
                scaleY = scale
            }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            MysticFrame(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 10.dp, vertical = 12.dp)
                    .size(width = 340.dp, height = 210.dp),
                alpha = borderAlpha,
                flash = flash
            )
            MysticSparkles(
                modifier = Modifier.matchParentSize(),
                progress = sparkle,
                flash = flash
            )
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = MaellenFamily,
                    fontSize = 52.sp,
                    color = Color.White.copy(alpha = 0.95f)
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
                    .blur(0.2.dp)
                    .graphicsLayer {
                        shadowElevation = 18f
                    },
                maxLines = 2
            )
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = MaellenFamily,
                    fontSize = 52.sp,
                    color = Color.White.copy(alpha = 0.26f + flash * 0.28f)
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
                    .blur(12.dp),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun MysticFrame(
    modifier: Modifier,
    alpha: Float,
    flash: Float
) {
    if (alpha <= 0f) return
    Canvas(modifier = modifier) {
        val strokeA = (0.65f * alpha).coerceIn(0f, 1f)
        val w = size.width
        val h = size.height

        val outerCorner = CornerRadius(34f, 34f)
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFE6D4FF).copy(alpha = strokeA),
                    Color(0xFF9F7BFF).copy(alpha = strokeA)
                )
            ),
            size = Size(w, h),
            cornerRadius = outerCorner,
            style = Stroke(width = 4f)
        )

        val inset = 16f
        drawRoundRect(
            color = Color(0xFF6F4CC5).copy(alpha = 0.45f * strokeA),
            topLeft = Offset(inset, inset),
            size = Size(w - inset * 2, h - inset * 2),
            cornerRadius = CornerRadius(22f, 22f),
            style = Stroke(
                width = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f), 6f)
            )
        )

        val cornerInset = 10f
        val arcRadius = 30f
        val cornerCenters = listOf(
            Offset(cornerInset + arcRadius, cornerInset + arcRadius),
            Offset(w - cornerInset - arcRadius, cornerInset + arcRadius),
            Offset(cornerInset + arcRadius, h - cornerInset - arcRadius),
            Offset(w - cornerInset - arcRadius, h - cornerInset - arcRadius)
        )
        cornerCenters.forEach { c ->
            drawArc(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE8DBFF).copy(alpha = 0.8f * strokeA),
                        Color.Transparent
                    ),
                    center = c,
                    radius = arcRadius * (1.2f + flash * 0.6f)
                ),
                startAngle = -35f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(arcRadius * 2, arcRadius * 2),
                topLeft = Offset(c.x - arcRadius, c.y - arcRadius),
                style = Stroke(width = 3f)
            )
        }
    }
}

@Composable
private fun MysticSparkles(
    modifier: Modifier,
    progress: Float,
    flash: Float
) {
    if (progress <= 0f) return
    val specs = listOf(
        SparkleSpec(0.18f, 0.25f, 18f, 0f),
        SparkleSpec(0.82f, 0.26f, 14f, 0.08f),
        SparkleSpec(0.14f, 0.72f, 16f, 0.12f),
        SparkleSpec(0.86f, 0.72f, 20f, 0.18f),
        SparkleSpec(0.50f, 0.10f, 22f, 0.15f),
        SparkleSpec(0.50f, 0.90f, 18f, 0.22f),
        SparkleSpec(0.30f, 0.48f, 12f, 0.30f),
        SparkleSpec(0.70f, 0.52f, 12f, 0.34f)
    )

    Canvas(modifier = modifier) {
        val scaleBase = min(size.width, size.height) / 360f
        specs.forEachIndexed { index, spec ->
            val local = (progress - spec.delay).coerceIn(0f, 1f)
            if (local <= 0f) return@forEachIndexed
            val pulse = 0.6f + 0.6f * sin((local + index * 0.2f) * PI).toFloat()
            val alpha = (0.18f + 0.75f * local) * (1f + flash * 0.4f)

            val x = size.width * spec.x
            val y = size.height * spec.y
            val sizePx = spec.size * scaleBase * pulse
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = alpha),
                        Color(0xFFD6C7FF).copy(alpha = alpha * 0.6f),
                        Color.Transparent
                    ),
                    center = Offset(x, y),
                    radius = sizePx * 2.6f
                ),
                radius = sizePx * 2.6f,
                center = Offset(x, y),
                blendMode = BlendMode.Screen
            )

            val stroke = 1.6f * scaleBase
            drawLine(
                color = Color.White.copy(alpha = 0.85f * alpha),
                start = Offset(x - sizePx, y),
                end = Offset(x + sizePx, y),
                strokeWidth = stroke
            )
            drawLine(
                color = Color.White.copy(alpha = 0.85f * alpha),
                start = Offset(x, y - sizePx),
                end = Offset(x, y + sizePx),
                strokeWidth = stroke
            )
        }
    }
}

private data class SparkleSpec(
    val x: Float,
    val y: Float,
    val size: Float,
    val delay: Float
)
