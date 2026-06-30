package com.myowntrip.app.ui.features.journal.spread

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.tooling.preview.Preview
import com.myowntrip.app.ui.brand.BrandColors
import com.myowntrip.app.ui.brand.BrandRibbon
import com.myowntrip.app.ui.theme.FrauncesFamily
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/* ------------------------------------------------------------------ */
/*  Diario · Álbum scrapbook (PROOF / demo)                            */
/*  Hojas que pasas (HorizontalPager). Día con varias hojas; días =    */
/*  capítulos. Libertad asistida dentro de cada hoja (drag + snap).    */
/* ------------------------------------------------------------------ */

private val Hair = Color(0xFFDED7C8)
private val Print = Color(0xFFFCFBF7)
private val Hand = FrauncesFamily // TODO: fuente manuscrita (Caveat) bundled
private val GridDp = 18.dp

private data class DemoPage(
    val day: Int, val place: String, val date: String,
    val pageNum: Int, val pageTotal: Int, val chapter: Boolean,
)

private val demoPages = listOf(
    DemoPage(3, "Madrid", "viernes 12 jun", 1, 2, true),
    DemoPage(3, "Madrid", "viernes 12 jun", 2, 2, false),
    DemoPage(4, "Sevilla", "sábado 13 jun", 1, 1, true),
)

@Composable
fun ScrapbookAlbumDemo(modifier: Modifier = Modifier) {
    val state = rememberPagerState(pageCount = { demoPages.size })
    val scope = rememberCoroutineScope()

    Column(modifier.fillMaxSize().background(Color(0xFFE7E0D6))) {
        HorizontalPager(
            state = state,
            modifier = Modifier.fillMaxSize().weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
        ) { i ->
            Sheet(demoPages[i]) {
                when (i) {
                    0 -> MadridPage1()
                    1 -> MadridPage2()
                    else -> SevillaPage()
                }
            }
        }

        // chrome inferior: indicador + salto de día + FAB
        val cur = demoPages[state.currentPage]
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Día ${cur.day} · ${cur.pageNum}/${cur.pageTotal}",
                fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandColors.Ink)
            Spacer(Modifier.width(16.dp))
            listOf(3, 4).forEach { d ->
                val idx = demoPages.indexOfFirst { it.day == d }
                val active = cur.day == d
                Box(
                    Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (active) BrandColors.Ink else Color.Transparent)
                        .border(1.dp, BrandColors.Ink.copy(alpha = if (active) 0f else 0.35f), RoundedCornerShape(50))
                        .clickable { scope.launch { state.animateScrollToPage(idx) } }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                ) {
                    Text("Día $d", fontSize = 12.sp, fontWeight = FontWeight.Medium,
                        color = if (active) Color.White else BrandColors.Ink)
                }
            }
            Spacer(Modifier.weight(1f))
            Box(
                Modifier.size(48.dp).shadow(6.dp, CircleShape).background(BrandColors.Ink, CircleShape),
                contentAlignment = Alignment.Center,
            ) { Text("+", color = Color.White, fontSize = 24.sp) }
        }
    }
}

/** Una hoja física: card papel con masthead + lienzo dot-grid. */
@Composable
private fun Sheet(page: DemoPage, content: @Composable BoxScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize()
            .padding(vertical = 12.dp)
            .shadow(10.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(BrandColors.Paper),
    ) {
        Masthead(page)
        Box(
            Modifier.fillMaxSize().clipToBounds()
                .drawBehind {
                    val step = GridDp.toPx(); val r = 1.1.dp.toPx()
                    var y = 8.dp.toPx()
                    while (y < size.height) {
                        var x = 8.dp.toPx()
                        while (x < size.width) { drawCircle(Hair, r, Offset(x, y)); x += step }
                        y += step
                    }
                },
            content = content,
        )
    }
}

@Composable
private fun Masthead(page: DemoPage) {
    Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)) {
        if (page.chapter) {
            Text("DÍA ${page.day}", fontFamily = FrauncesFamily, fontWeight = FontWeight.Medium,
                fontSize = 11.sp, letterSpacing = 1.8.sp, color = BrandColors.AccentOcre)
            Text(page.place, fontFamily = FrauncesFamily, fontWeight = FontWeight.Medium,
                fontSize = 26.sp, color = BrandColors.Ink)
            Text(page.date, fontSize = 12.sp, color = BrandColors.Ink.copy(alpha = 0.75f))
        } else {
            Text("${page.place} · hoja ${page.pageNum}", fontFamily = FrauncesFamily,
                fontStyle = FontStyle.Italic, fontSize = 16.sp, color = BrandColors.Ink.copy(alpha = 0.7f))
        }
    }
}

/* ---------------- contenido por hoja ---------------- */

@Composable
private fun BoxScope.MadridPage1() {
    DoodleArrow(Modifier.offset(150.dp, 96.dp))
    ScrapPiece(14.dp, 16.dp, -4f) { Polaroid("la luz de Goya ✶", Color(0xFFE2C9A0), Color(0xFFC98F63)) }
    ScrapPiece(200.dp, 12.dp, 7f) { Stamp("MADRID", "12 · JUN") }
    ScrapPiece(214.dp, 110.dp, -9f) { Sticker("☕") }
    ScrapPiece(20.dp, 188.dp, -1.5f) { Banner("Día tranquilo.") }
    ScrapPiece(26.dp, 232.dp, -2.5f) { AudioChip() }
    ScrapPiece(140.dp, 232.dp, 2f) { HandNote("café con leche antes de volver — la mejor esquina del barrio") }
    ScrapPiece(24.dp, 312.dp, 1f) { EventPiece("PRADO · 11:00", "Impresionante la colección permanente.") }
}

@Composable
private fun BoxScope.MadridPage2() {
    ScrapPiece(150.dp, 18.dp, 5f) { Polaroid("Retiro al atardecer", Color(0xFFBFD0C4), Color(0xFF6E8C7C)) }
    ScrapPiece(20.dp, 40.dp, -2f) { HandNote("paseo larguísimo, pies muertos pero mereció la pena") }
    ScrapPiece(28.dp, 210.dp, 1.5f) { Banner("Tapas en Lavapiés.") }
    ScrapPiece(170.dp, 250.dp, -7f) { Sticker("🍷") }
    DoodleArrow(Modifier.offset(60.dp, 250.dp))
}

@Composable
private fun BoxScope.SevillaPage() {
    Column(
        Modifier.align(Alignment.Center).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Esta hoja está en blanco.", fontFamily = FrauncesFamily, fontSize = 18.sp, color = BrandColors.Ink)
        Text("Pega tu primer recuerdo con +.", fontSize = 13.sp, color = BrandColors.Ink.copy(alpha = 0.6f))
    }
    ScrapPiece(140.dp, 60.dp, 6f) { Stamp("SEVILLA", "13 · JUN") }
}

/* ---------------- pieza arrastrable ---------------- */

@Composable
private fun ScrapPiece(initX: Dp, initY: Dp, rotation: Float, content: @Composable () -> Unit) {
    val density = LocalDensity.current
    val grid = with(density) { GridDp.toPx() }
    var pos by remember { mutableStateOf(with(density) { Offset(initX.toPx(), initY.toPx()) }) }
    var z by remember { mutableStateOf(0f) }
    Box(
        Modifier
            .zIndex(z)
            .offset { IntOffset(pos.x.roundToInt(), pos.y.roundToInt()) }
            .graphicsLayer { rotationZ = rotation }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { z = 1f },
                    onDragEnd = {
                        pos = Offset((pos.x / grid).roundToInt() * grid, (pos.y / grid).roundToInt() * grid)
                    },
                    onDrag = { change, drag -> change.consume(); pos += drag },
                )
            },
    ) { content() }
}

/* ---------------- piezas ---------------- */

@Composable
private fun Polaroid(caption: String, c1: Color, c2: Color) {
    Box {
        Column(
            Modifier.shadow(7.dp, RoundedCornerShape(2.dp)).background(Print, RoundedCornerShape(2.dp))
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 26.dp).width(150.dp),
        ) {
            Box(Modifier.fillMaxWidth().height(112.dp)
                .background(Brush.linearGradient(listOf(c1, c2)), RoundedCornerShape(1.dp)))
            Text(caption, fontFamily = Hand, fontStyle = FontStyle.Italic, fontSize = 16.sp,
                color = BrandColors.Ink, modifier = Modifier.padding(top = 6.dp).align(Alignment.CenterHorizontally))
        }
        Box(Modifier.offset((-8).dp, (-6).dp).graphicsLayer { rotationZ = -32f }
            .width(54.dp).height(18.dp).background(BrandColors.AccentOcre.copy(alpha = 0.42f)))
    }
}

@Composable
private fun Stamp(city: String, date: String) {
    Column(
        Modifier.border(2.dp, BrandColors.AccentOcre, RoundedCornerShape(8.dp)).padding(3.dp)
            .border(1.dp, BrandColors.AccentOcre.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp).graphicsLayer { alpha = 0.82f },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(city, fontFamily = FrauncesFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
            letterSpacing = 1.2.sp, color = BrandColors.AccentOcre)
        Text("$date · 2026", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.6.sp,
            color = BrandColors.AccentOcre)
        Text("✈", fontSize = 13.sp, color = BrandColors.AccentOcre)
    }
}

@Composable
private fun Sticker(emoji: String) {
    Box(
        Modifier.shadow(6.dp, CircleShape).background(Color.White, CircleShape).padding(5.dp)
            .size(76.dp).clip(CircleShape)
            .background(Brush.radialGradient(listOf(Color(0xFFD9B48C), Color(0xFF9C6B3F)))),
        contentAlignment = Alignment.Center,
    ) { Text(emoji, fontSize = 30.sp) }
}

@Composable
private fun Banner(text: String) {
    Text(text, fontFamily = FrauncesFamily, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Medium,
        fontSize = 20.sp, color = BrandColors.Ink,
        modifier = Modifier.drawBehind {
            val y = size.height - 1.dp.toPx()
            drawLine(BrandColors.AccentOcre, Offset(0f, y), Offset(size.width, y), 2.dp.toPx())
        })
}

@Composable
private fun HandNote(text: String) {
    Box(Modifier.width(150.dp)) {
        Text(text, fontFamily = Hand, fontStyle = FontStyle.Italic, fontSize = 17.sp,
            color = BrandColors.Ink, lineHeight = 22.sp)
    }
}

@Composable
private fun AudioChip() {
    Row(
        Modifier.shadow(4.dp, RoundedCornerShape(20.dp)).background(Print, RoundedCornerShape(20.dp))
            .border(1.dp, Hair, RoundedCornerShape(20.dp)).padding(start = 8.dp, end = 12.dp, top = 7.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(Modifier.size(26.dp).border(1.5.dp, BrandColors.Ink, CircleShape), contentAlignment = Alignment.Center) {
            Text("▶", fontSize = 10.sp, color = BrandColors.Ink)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
            listOf(8, 16, 11, 20, 14, 18, 9, 15).forEach { h ->
                Box(Modifier.width(2.5.dp).height(h.dp).background(BrandColors.Ink.copy(alpha = 0.6f), RoundedCornerShape(2.dp)))
            }
        }
    }
}

@Composable
private fun EventPiece(label: String, text: String) {
    Box {
        Column(
            Modifier.shadow(5.dp, RoundedCornerShape(3.dp)).background(Print, RoundedCornerShape(3.dp))
                .border(1.dp, Hair, RoundedCornerShape(3.dp)).padding(horizontal = 13.dp, vertical = 11.dp).width(150.dp),
        ) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, color = BrandColors.Ink)
            Text(text, fontFamily = FrauncesFamily, fontSize = 14.sp, color = BrandColors.Ink, lineHeight = 18.sp,
                modifier = Modifier.padding(top = 4.dp))
        }
        BrandRibbon(height = 26.dp, modifier = Modifier.align(Alignment.TopEnd).padding(end = 14.dp).offset(y = (-2).dp))
    }
}

@Composable
private fun DoodleArrow(modifier: Modifier) {
    Box(modifier.size(90.dp, 70.dp).drawBehind {
        val s = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        val p = androidx.compose.ui.graphics.Path().apply {
            moveTo(5.dp.toPx(), 60.dp.toPx())
            cubicTo(30.dp.toPx(), 50.dp.toPx(), 20.dp.toPx(), 15.dp.toPx(), 55.dp.toPx(), 18.dp.toPx())
        }
        drawPath(p, BrandColors.Ink, style = s)
        val head = androidx.compose.ui.graphics.Path().apply {
            moveTo(48.dp.toPx(), 12.dp.toPx()); lineTo(57.dp.toPx(), 18.dp.toPx()); lineTo(49.dp.toPx(), 26.dp.toPx())
        }
        drawPath(head, BrandColors.Ink, style = s)
    })
}

@Preview(name = "Diario · álbum", showBackground = true, widthDp = 360, heightDp = 760)
@Composable
fun ScrapbookAlbumPreview() {
    JournalSpreadPreviewTheme {
        JournalSpreadPreviewFrame(height = 760.dp) {
            ScrapbookAlbumDemo(Modifier.fillMaxSize())
        }
    }
}
