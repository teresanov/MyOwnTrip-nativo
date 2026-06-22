package com.myowntrip.app.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myowntrip.app.ui.brand.BrandColors
import com.myowntrip.app.ui.brand.BrandRibbon
import com.myowntrip.app.ui.theme.FrauncesFamily
import com.myowntrip.app.ui.theme.InterFamily
import com.myowntrip.app.ui.theme.LocalReduceMotion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/* ------------------------------------------------------------------ */
/*  MyOwnTrip · Splash                                                  */
/*  Morph de letras: M·O·T persisten, el resto colapsa → MOT → icono.   */
/*  Sin cinta durante el morph; la cinta real (BrandRibbon) aparece     */
/*  SOLO en el icono final → cero descoloques.                          */
/* ------------------------------------------------------------------ */

private val Decel = CubicBezierEasing(0.2f, 0f, 0f, 1f)
private fun lerp(a: Float, b: Float, t: Float) = a + (b - a) * t

/** Colapsa el ancho del hijo a 0 según [progress] (0..1), centrándolo. */
private fun Modifier.collapseWidth(progress: Float): Modifier = layout { measurable, constraints ->
    val p = measurable.measure(constraints)
    val w = (p.width * (1f - progress)).roundToInt().coerceAtLeast(0)
    layout(w, p.height) { p.place(((w - p.width) / 2), 0) }
}

@Composable
fun SplashScreen(
    onDone: () -> Unit,
    playFullMorph: Boolean = true,
) {
    val reduceMotion = LocalReduceMotion.current

    val inAlpha = remember { Animatable(0f) }
    val inScale = remember { Animatable(0.95f) }
    val condense = remember { Animatable(0f) } // wordmark → MOT
    val icon = remember { Animatable(0f) }     // MOT → icono
    val words = remember { List(3) { Animatable(0f) } }
    val exit = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        if (reduceMotion || !playFullMorph) {
            inAlpha.snapTo(1f); inScale.snapTo(1f); icon.snapTo(1f)
            words.forEach { it.snapTo(1f) }
            delay(1400); onDone(); return@LaunchedEffect
        }
        delay(100)
        launch { inAlpha.animateTo(1f, tween(350, easing = Decel)) }
        inScale.animateTo(1f, tween(350, easing = Decel))
        delay(550)
        condense.animateTo(1f, tween(650, easing = Decel))
        delay(150)
        icon.animateTo(1f, tween(650, easing = Decel))
        delay(250)
        words.forEach { w ->
            launch { w.animateTo(1f, tween(600, easing = Decel)) }
            delay(420)
        }
        delay(1000)
        exit.animateTo(1f, tween(450, easing = Decel))
        onDone()
    }

    Box(Modifier.fillMaxSize().background(BrandColors.Paper), contentAlignment = Alignment.Center) {
        Box(
            Modifier.graphicsLayer {
                alpha = 1f - exit.value
                val s = lerp(1f, 0.98f, exit.value); scaleX = s; scaleY = s
            },
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .offset(y = (-36).dp)
                    .graphicsLayer { alpha = inAlpha.value; scaleX = inScale.value; scaleY = inScale.value },
                contentAlignment = Alignment.Center
            ) {
                // Marco-cuaderno (detrás), aparece con el icono
                Box(
                    Modifier
                        .size(108.dp)
                        .graphicsLayer {
                            alpha = icon.value
                            val s = lerp(0.82f, 1f, icon.value); scaleX = s; scaleY = s
                        }
                        .background(BrandColors.Paper, RoundedCornerShape(24.dp))
                        .border(1.6.dp, BrandColors.Ink, RoundedCornerShape(24.dp))
                )

                // Letras que hacen el morph (la M sobrevive y se vuelve la M del icono)
                BrandLetters(condense.value, icon.value)

                // Cinta REAL, solo en el icono (BrandRibbon = drawable, colocada como el componente)
                BrandRibbon(
                    height = 34.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp)
                        .offset(y = (-6).dp)
                        .graphicsLayer { alpha = icon.value },
                )
            }

            // Tagline
            Column(
                Modifier.offset(y = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Planifica.", "Guarda.", "Revive.").forEachIndexed { i, w ->
                    val a = words[i].value
                    Text(
                        text = w,
                        fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 18.sp,
                        color = BrandColors.Ink, textAlign = TextAlign.Center,
                        modifier = Modifier.graphicsLayer { alpha = a; translationY = (1f - a) * 8.dp.toPx() }
                    )
                }
            }
        }
    }
}

/** "MyOwnTrip" — M·O·T persisten; el resto colapsa. "Own" en ocre. Sin cinta. */
@Composable
private fun BrandLetters(condense: Float, icon: Float) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.graphicsLayer { translationY = icon * 3.dp.toPx() }, // centra la M en el marco
    ) {
        val faded = 1f - condense
        val muted = lerp(1f, BrandColors.MotMutedLayerOpacity, condense)
        Letter("M", size = lerp(lerp(38f, 58f, condense), 62f, icon),
            color = BrandColors.Ink, alpha = lerp(muted, 1f, icon))
        Letter("y", 38f, BrandColors.Ink, faded, modifier = Modifier.collapseWidth(condense))
        Letter("O", size = lerp(38f, 58f, condense), color = BrandColors.AccentOcre,
            alpha = 1f - icon, italic = condense < 0.5f, modifier = Modifier.collapseWidth(icon))
        Letter("w", 38f, BrandColors.AccentOcre, faded, italic = true, modifier = Modifier.collapseWidth(condense))
        Letter("n", 38f, BrandColors.AccentOcre, faded, italic = true, modifier = Modifier.collapseWidth(condense))
        Letter("T", size = lerp(38f, 58f, condense), color = BrandColors.Ink,
            alpha = muted * (1f - icon), modifier = Modifier.collapseWidth(icon))
        Letter("r", 38f, BrandColors.Ink, faded, modifier = Modifier.collapseWidth(condense))
        Letter("i", 38f, BrandColors.Ink, faded, modifier = Modifier.collapseWidth(condense))
        Letter("p", 38f, BrandColors.Ink, faded, modifier = Modifier.collapseWidth(condense))
    }
}

@Composable
private fun Letter(
    char: String, size: Float, color: Color, alpha: Float,
    italic: Boolean = false, modifier: Modifier = Modifier,
) {
    Text(
        text = char,
        fontFamily = FrauncesFamily, fontWeight = FontWeight.Medium,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        fontSize = size.sp, color = color,
        modifier = modifier.graphicsLayer { this.alpha = alpha }
    )
}
