import{t as M}from"./tokens-GdfYCzkC.js";import{a as P,b as V}from"./bootstrap-cWH7yJsg.js";const D=`package com.myowntrip.app.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Espaciado de **producto / pantallas** (SoT nativo).
 *
 * Paridad Figma: colección **MOT · Spacing** (\`mot/spacing/…\`) → alias de **Primitives · Space**.
 * Ver \`docs/design-system/spacing.md\`.
 *
 * Solo layout de pantalla / entre bloques — no sustituye el padding interno de componentes M3
 * (Button, TextField, Chip, …). No usar la colección legado Semantic · Layout.
 */
object MOTSpacing {
  val none = 0.dp
  val screenHorizontal = 16.dp
  /** Colchón inferior en contenido scrolleable (zona segura gesto / barra / FAB). */
  val screenContentBottom = 24.dp
  val layoutMd = 16.dp
  val layoutLg = 24.dp
  val componentSm = 8.dp
  /** Inset/gap componente md (12). Figma: \`mot/spacing/component-md\` → alias \`spacing/12\`. */
  val componentMd = 12.dp
  /** Inset/gap componente lg (16). Figma: \`mot/spacing/component-lg\` → alias \`spacing/16\`. */
  val componentLg = 16.dp
  val componentXs = 4.dp
  val gutterGrid = 16.dp
  /** Lista con FAB flotante en la misma pantalla. */
  val screenContentBottomWithFab = 88.dp

  /**
   * Padding interno Button Size=XSmall (Figma State-layer / [MOTButtonXSmallContentPadding]).
   * Semánticos Figma: \`mot/padding/button/xsmall-horizontal|vertical\` (colección MOT · Spacing).
   */
  object ButtonXSmall {
    val horizontal = 12.dp
    val vertical = 6.dp
  }

  /**
   * Menu de producto (Home filtros). Figma: \`mot/spacing/menu/group-gap\`.
   * Contenedor: \`MaterialTheme.shapes.large\` (16) ↔ \`Corner/Large\` — no literales sueltos.
   */
  object Menu {
    val groupGap = 2.dp
    /** Figma: \`mot/padding/menu/vertical\` y \`horizontal\`. */
    val vertical = 8.dp
    val horizontal = 0.dp
  }

  /**
   * List item / swipe reveal. Figma: \`mot/padding/list-item/…\` · \`mot/spacing/list-item/gap\`.
   * El set M3 \`List Item: 0 Density\` usa [layoutMd] (16) entre leading y texto;
   * [gap] 12 es el hueco del swipe. [trailingGap] = icono trailing y meta.
   */
  object ListItem {
    val horizontal = 16.dp
    val vertical = 12.dp
    val gap = 12.dp
    val trailingGap = 10.dp
  }

  /**
   * Date / time pickers. Figma kit M3 celdas = 10; rejilla producto Mot* = [dayGridGap].
   * \`mot/spacing/date-picker/cell\` · \`mot/padding/date-picker/cell\` · \`mot/spacing/date-picker/day-grid-gap\`.
   */
  object DatePicker {
    val cell = 10.dp
    val dayGridGap = 2.dp
    /**
     * Hueco entre bloques de la cabecera del set Input. Figma: \`mot/spacing/date-picker/header-gap\`.
     * Reservado: sin consumidor en Compose hasta que exista la estructura cabecera + dos campos.
     */
    val headerGap = 36.dp
    /**
     * Padding superior del campo de fecha (set Input). Figma: \`mot/padding/date-picker/field-top\`.
     * Reservado: sin consumidor en Compose (ver \`headerGap\`).
     */
    val fieldTop = 18.dp
    /**
     * Hueco de \`Label and Date\` en el set Modal. Figma: \`mot/spacing/date-picker/label-gap\`.
     * Reservado: sin consumidor en Compose.
     */
    val labelGap = 36.dp
    /**
     * Hueco de \`Local Selection Row\` en el set Modal. Figma: \`mot/spacing/date-picker/selection-row-gap\`.
     * Reservado: sin consumidor en Compose.
     */
    val selectionRowGap = 74.dp
    /**
     * Hueco del bloque \`Frame 2608508\` en el set Modal. Figma: \`mot/spacing/date-picker/block-gap\`.
     * Reservado: sin consumidor en Compose.
     */
    val blockGap = 244.dp
  }

  /**
   * Tabs M3 (Primary label). Figma: \`mot/padding/tab/…\` · \`mot/spacing/tab/gap\`.
   * Compose [MOTTab] / TabRow; pads vía [Tab.horizontal] / [Tab.vertical].
   */
  object Tab {
    val horizontal = 16.dp
    val vertical = 14.dp
    val gap = 4.dp
    val indicatorInset = 2.dp
  }

  /**
   * Adjunto wallet. Figma: set Document attachment · preview altura fija.
   * Pads del card = [layoutMd]; gaps hint/footer = [componentSm] / [componentXs].
   */
  object DocumentAttachment {
    val previewHeight = 180.dp
  }

  /**
   * Campo de hora (M3 TimeInput) en el kit. Figma: \`mot/spacing/time-input/…\`.
   * fieldGap = hueco interno del campo (1). labelTop (7) sin uso hoy: reservado para un label bajo el campo.
   */
  object TimeInput {
    /** Reservado: sin uso en Compose ni en el set \`Input\` de Figma. */
    val labelTop = 7.dp
    val fieldGap = 1.dp
    val contentGap = 10.dp
    /** Hueco entre el campo de hora y la etiqueta "Time label" (Direct Input). */
    val labelGap = 8.dp
  }

  /** Botón por tamaño. Figma: \`mot/padding/button/{small,medium,large}-*\`. Small lo usa \`MOTButton\` (\`MOTButtonSmallContentPadding\`). */
  object ButtonSmall {
    /** Figma = código: el padding por defecto de \`MOTButton\` (M3 \`ContentPadding\`) es 24 horizontal y 8 vertical. */
    val horizontal = 24.dp
    val vertical = 8.dp
  }

  object ButtonMedium {
    val horizontal = 24.dp
    val vertical = 16.dp
  }

  object ButtonLarge {
    val horizontal = 48.dp
    val vertical = 32.dp
  }

  /** Chip. Figma: \`mot/padding/chip/…\` y \`mot/spacing/chip/gap\`. */
  object Chip {
    val horizontalSm = 8.dp
    val horizontalLg = 16.dp
    val vertical = 6.dp
    val gap = 8.dp
  }

  /** Diálogo. Figma: \`mot/padding/dialog/…\` y \`mot/spacing/dialog/…\`. */
  object Dialog {
    val container = 24.dp
    val sectionGap = 16.dp
    /** Hueco alrededor del divisor. Figma: \`mot/spacing/dialog/divider-gap\`. */
    val dividerGap = 12.dp
    val actionsGap = 8.dp
  }

  /** FAB por tamaño. Figma: \`mot/padding/fab/{default,medium,large}\`. */
  object Fab {
    val default = 16.dp
    val medium = 26.dp
    val large = 30.dp
  }

  /** Snackbar. Figma: \`mot/padding/snackbar/…\` y \`mot/spacing/snackbar/…\`. */
  object Snackbar {
    val horizontal = 16.dp
    val vertical = 12.dp
    val gap = 12.dp
    val gapSm = 8.dp
    val gapXs = 4.dp
  }

  /**
   * Teclado del selector de hora (Keyboard picker). Figma: \`mot/spacing/keyboard-picker/gap\` (alias de \`spacing/20\`).
   * Reservado: sin consumidor en Compose, que usa el \`TimePicker\` de M3.
   */
  object KeyboardPicker {
    val gap = 20.dp
  }

  /**
   * Padding interno del icon button (State-layer). Figma: \`mot/padding/icon-button/…\`.
   * Tamaño por defecto M3 (Small, 40 dp) no usa padding; wide queda en Figma para uso futuro.
   */
  object IconButton {
    val zeroHorizontal = 0.dp
    val zeroVertical = 0.dp
    val wideHorizontal = 32.dp
    val wideVertical = 16.dp
    val tenHorizontal = 10.dp
    val tenVertical = 10.dp
    val lgHorizontal = 32.dp
    val lgVertical = 32.dp
    val mdHorizontal = 16.dp
    val mdVertical = 16.dp
  }

  /**
   * Padding interno del campo de texto (Outlined). Figma: \`mot/padding/text-field/horizontal|vertical\`.
   * Compose: consumir desde el TextField de M3 cuando se personalice; el set Input usa estos valores.
   */
  object TextField {
    val horizontal = 16.dp
    val vertical = 4.dp
    /** Campo outlined (\`OutlinedTextField\`, M3): 16 arriba y abajo, con o sin etiqueta. Figma: \`mot/padding/text-field/outlined-vertical\`. */
    val outlinedVertical = 16.dp
  }
}
`,R=`package com.myowntrip.app.ui.theme

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * SoT de shape de superficie: roles M3 ([Shapes]) ↔ Figma colección Shape (\`Corner/…\`).
 *
 * Consumo en UI: \`MaterialTheme.shapes.extraSmall|small|medium|large|extraLarge\`.
 * No hay un segundo objeto semántico paralelo (\`MOTCorner\`).
 *
 * | M3 / Compose | Figma | dp |
 * |--------------|-------|---:|
 * | \`shapes.extraSmall\` | \`Corner/Extra-small\` | 4 |
 * | \`shapes.small\` | \`Corner/Small\` | 8 |
 * | \`shapes.medium\` | \`Corner/Medium\` | 12 |
 * | \`shapes.large\` | \`Corner/Large\` | 16 |
 * | \`shapes.extraLarge\` | \`Corner/Extra-large\` | 28 |
 *
 * Botones: [rememberMOTButtonShape] (ADR 004) — fuera de la escala \`shapes.*\`.
 */
val MOTThemeShapes = Shapes(
  extraSmall = RoundedCornerShape(4.dp),
  small = RoundedCornerShape(8.dp),
  medium = RoundedCornerShape(12.dp),
  large = RoundedCornerShape(16.dp),
  extraLarge = RoundedCornerShape(28.dp),
)

/** Radios del morph de botón (ADR 004). No forman parte de [MOTThemeShapes]. */
private object MotButtonCornerDp {
  val Rest = 0.dp
  val Active = 20.dp
}

/**
 * Morph 0dp → 20dp en hover, focus y pressed — asimétrico (ADR 004 rev. squish + muelle):
 * entrada rápida (el botón reacciona al dedo) y liberación con muelle que rebasa y asienta.
 * Para selected en toggles, pasar [forceActive] = true.
 *
 * Figma: \`Corner/None\` ↔ \`Corner/Large-increased\`.
 */
@Composable
fun rememberMOTButtonShape(
  interactionSource: MutableInteractionSource = MutableInteractionSource(),
  forceActive: Boolean = false,
): Shape {
  val pressed by interactionSource.collectIsPressedAsState()
  val focused by interactionSource.collectIsFocusedAsState()
  val hovered by interactionSource.collectIsHoveredAsState()
  val interactive = forceActive || pressed || focused || hovered
  val reduceMotion = LocalReduceMotion.current

  val corner by animateDpAsState(
    targetValue = if (interactive) MotButtonCornerDp.Active else MotButtonCornerDp.Rest,
    animationSpec = when {
      reduceMotion -> snap()
      interactive -> AppMotion.pressInSpec()
      else -> AppMotion.releaseSpringSpec(Dp.VisibilityThreshold)
    },
    label = "motButtonCorner",
  )

  // El muelle de liberación rebasa 0dp; Compose no admite radios negativos en RoundedCornerShape.
  return RoundedCornerShape(corner.coerceAtLeast(0.dp))
}

/**
 * Squish de pulsación: el dedo comprime el botón a [AppMotion.PressedButtonScale] con la
 * misma entrada rápida que la esquina; al soltar, muelle con ligero rebase. Solo actúa en
 * pressed — hover y focus morfan la esquina sin escalar. Pasar el MISMO interactionSource
 * que recibe el botón, o la escala nunca se disparará.
 */
@Composable
fun Modifier.motButtonPressSquish(interactionSource: InteractionSource): Modifier {
  val pressed by interactionSource.collectIsPressedAsState()
  val reduceMotion = LocalReduceMotion.current

  val scale by animateFloatAsState(
    targetValue = if (pressed) AppMotion.PressedButtonScale else 1f,
    animationSpec = when {
      reduceMotion -> snap()
      pressed -> AppMotion.pressInSpec()
      else -> AppMotion.releaseSpringSpec()
    },
    label = "motButtonSquish",
  )

  return graphicsLayer {
    scaleX = scale
    scaleY = scale
  }
}
`,q=`package com.myowntrip.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Familias en Wasm/Desktop. Arrancan con las genéricas del sistema; quien hospeda el DS
 * (la galería o las demos del showcase) puede cargar los .ttf reales de designsystem y
 * asignarlos aquí ANTES de la primera composición.
 */
object MotSkiaFonts {
  var fraunces: FontFamily = FontFamily.Serif
  var inter: FontFamily = FontFamily.SansSerif
  var hand: FontFamily = FontFamily.Cursive
}

actual val FrauncesFamily: FontFamily get() = MotSkiaFonts.fraunces
actual val InterFamily: FontFamily get() = MotSkiaFonts.inter
actual val HandFamily: FontFamily get() = MotSkiaFonts.hand

actual val AppTypography: Typography get() = motTypography()

private fun motTypography() = Typography(
  displayLarge = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
  displayMedium = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 45.sp, lineHeight = 52.sp),
  displaySmall = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 36.sp, lineHeight = 44.sp),
  headlineLarge = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 32.sp, lineHeight = 40.sp),
  headlineMedium = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Normal, fontSize = 28.sp, lineHeight = 36.sp),
  headlineSmall = TextStyle(fontFamily = FrauncesFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp),
  titleLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 22.sp, lineHeight = 28.sp),
  titleMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
  titleSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
  bodyLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
  bodyMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
  bodySmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),
  labelLarge = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
  labelMedium = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
  labelSmall = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)
`;function E(e){const n={},a=[];for(const t of e.split(/\r?\n/)){const o=t.trim(),s=o.match(/^object (\w+)\s*\{/);if(s){a.push(s[1]);continue}const i=o.match(/^val (\w+)\s*=\s*([\d.]+)\.dp/);i&&(n[[...a,i[1]].join(".")]=`${i[2]} dp`),o==="}"&&a.pop()}return n}function G(e){const n={};for(const a of e.matchAll(/(\w+)\s*=\s*RoundedCornerShape\(([\d.]+)\.dp\)/g))n[`shapes.${a[1]}`]=`radio ${a[2]} dp`;return n}function X(e){const n={},a={Normal:"Regular",Medium:"Medium",SemiBold:"SemiBold",Bold:"Bold"};for(const t of e.matchAll(/(\w+) = TextStyle\(fontFamily = (\w+)Family, fontWeight = FontWeight\.(\w+), fontSize = ([\d.]+)\.sp, lineHeight = ([\d.]+)\.sp/g))n[t[1]]=`${t[2]} ${a[t[3]]||t[3]} · ${t[4]} / ${t[5]} sp`;return n}const K=E(D),U=G(R),J=X(q);function Q(e,n){const a=n.trim();return e==="spacing"?K[a]||null:e==="shape"?U[a]||null:e==="type"&&J[a]||null}function Y(e){return e.split("/").map(n=>n.replace(/\(.*?\)/g,"").trim()).filter(Boolean).map(n=>{var t,o,s;const a=(t=M.extensions)==null?void 0:t[n];return{role:n,light:(a==null?void 0:a.light)||((o=M.light)==null?void 0:o[n])||null,dark:(a==null?void 0:a.dark)||((s=M.dark)==null?void 0:s[n])||null}})}function y(e){return String(e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")}const w='<span class="pending">Pendiente</span>';function x(e){return e||w}function W(e){return String(e).normalize("NFD").replace(/[̀-ͯ]/g,"").toLowerCase().replace(/<[^>]*>/g,"").replace(/[^a-z0-9]+/g,"-").replace(/^-|-$/g,"")}function r(e,n,{lead:a="",generated:t=!1,tocLabel:o=""}={}){const s=o?` data-toc-label="${o}"`:"";return`
  <section class="section">
    <h2 id="${e}"${s}>${n}${t?'<span class="badge-generated" title="Se deriva del código fuente; no se escribe a mano">desde el código</span>':""}</h2>
    ${a?`<p class="section-lead">${a}</p>`:""}`}function h(e,n){return`
    <table>
      <thead><tr>${e.map(a=>`<th>${a}</th>`).join("")}</tr></thead>
      <tbody>${n.map(a=>`<tr>${a.map(t=>`<td>${t}</td>`).join("")}</tr>`).join("")}</tbody>
    </table>`}function v(e,n="list-dos"){return`<ul class="${n}">${e.map(a=>`<li>${a}</li>`).join("")}</ul>`}function Z(e){return P(e)}function ee(e){const[n,a,t]=String(e||"").split("-");return n&&a&&t?`${t}/${a}/${n}`:e}function xe(e,n="Component set en Figma"){var i;const a=typeof e.figma=="string"?{url:e.figma}:e.figma||{},t=e.implementation||{},o=((i=e.usage)==null?void 0:i.purpose)||e.purpose,s=[a.url?`<a href="${a.url}" target="_blank" rel="noopener">${a.node?`Figma · ${a.node}`:n} ↗</a>`:"",t.sourceFile?`<span class="header-meta">Código: <code>${t.sourceFile}</code></span>`:""].filter(Boolean).join('<span class="header-sep">·</span>');return`
    <p class="page-header__eyebrow">Componente · ${e.priority||"P0"}</p>
    <h1>${e.name} ${Z(e.status)}</h1>
    <p class="page-header__lead">${x(o)}</p>
    ${s?`<p class="page-header__links">${s}</p>`:""}
    ${e.specimenHtml?ae(e.specimenHtml):""}
  `}function ae(e,n){return`
    <div class="specimen-stage">
      ${e}
    </div>
    <p class="specimen-stage__note">${e.includes("data-mot-specimen")?"El componente real de la app, compilado a WebAssembly.":"Recreación en HTML con los mismos roles y medidas que Compose (1&nbsp;px = 1&nbsp;dp). No es el componente real."}</p>
  `}function Se({doHtml:e,doCaption:n,dontHtml:a,dontCaption:t}){return`
    <div class="dodont-visual">
      <figure class="dodont-visual__card dodont-visual__card--do">
        <p class="dodont-visual__kicker">Bien</p>
        <div class="dodont-visual__stage">${e}</div>
        <figcaption>${n}</figcaption>
      </figure>
      <figure class="dodont-visual__card dodont-visual__card--dont">
        <p class="dodont-visual__kicker">Mal</p>
        <div class="dodont-visual__stage">${a}</div>
        <figcaption>${t}</figcaption>
      </figure>
    </div>
  `}function Fe(e){const n=e.usage;if(!n)return r("uso","Uso")+`<p>${w}</p></section>`;const a=(n.when||[]).length?`<h3 class="subsection-title">Cuándo usarlo</h3>${v(n.when)}`:"",t=(n.whenNot||[]).length?`<h3 class="subsection-title">Cuándo no usarlo</h3>
       ${h(["En este caso","Usa esto en su lugar"],n.whenNot.map(s=>[s.case,s.href?`<a href="${s.href}">${s.use}</a>`:s.use]))}`:"",o=(n.chooseVariant||[]).length?`<h3 class="subsection-title">Elegir variante</h3>
       <p class="section-lead">La jerarquía la marca la variante, no el color. Si dudas, empieza por la fila de arriba.</p>
       ${h(["Si…","Usa","Nota"],n.chooseVariant.map(s=>[s.if,`<code>${s.use}</code>`,s.note||""]))}`:"";return r("uso","Uso")+a+t+o+"</section>"}function Me(e){const n=e.guidelines,a=e.productRules,t=e.dos||[],o=e.donts||[];let s="";if(n&&(t.length||o.length)){const i=(n.links||[{label:n.label,url:n.url}]).filter(l=>l&&l.url).map(l=>`<a href="${l.url}" target="_blank" rel="noopener">${l.label} ↗</a>`).join(" · ");s+=r("do-dont","Do y Don't",{lead:`Guía oficial de Material Design 3 para este componente: ${i} (consultada el ${ee(n.consulted)}).`})+`<div class="guideline-grid">
        <div class="guideline-col guideline-col--do">
          <h3 class="subsection-title">Do</h3>${v(t)}
        </div>
        <div class="guideline-col guideline-col--dont">
          <h3 class="subsection-title">Don't</h3>${o.length?v(o,"list-dos list-dos--dont"):`<p class="section-lead">En las fuentes consultadas, M3 no lista «Don't» explícitos para este componente.</p>`}
        </div>
      </div></section>`}return a&&((a.dos||[]).length||(a.donts||[]).length)&&(s+=r("reglas","Reglas MyOwnTrip",{lead:n?"Acuerdos del equipo sobre cómo usamos el componente en MyOwnTrip. Complementan la guía de M3; no la contradicen.":"Componente propio de MyOwnTrip: no existe una guía de M3 equivalente, así que estas son las reglas del equipo."})+`<div class="guideline-grid">
        <div class="guideline-col guideline-col--do">
          <h3 class="subsection-title">Do</h3>${v(a.dos||[])}
        </div>
        <div class="guideline-col guideline-col--dont">
          <h3 class="subsection-title">Don't</h3>${v(a.donts||[],"list-dos list-dos--dont")}
        </div>
      </div></section>`),s}function Ce({id:e,title:n,hint:a,canvasHtml:t,controlsHtml:o=""}){return r(e||W(n),n,{lead:a})+`<div class="playground__frame">
      <div class="playground__canvas">${t}</div>
      ${o?`<div class="playground__controls">${o}</div>`:""}
    </div></section>`}function we(e,n="Variantes del set"){if(!(e!=null&&e.length))return"";const a=e.some(s=>s.composable),t=a?["Variante","Compose","Cuándo usarla"]:["Variante","Cuándo usarla"],o=e.map(s=>a?[s.label,s.composable?`<code>${s.composable}</code>`:w,s.usage]:[s.label,s.usage]);return r(W(n),n,{lead:"Cada variante tiene que poder trazarse a un composable real.",tocLabel:"Variantes"})+h(t,o)+"</section>"}function Te(e){var t,o;if(!e)return"";const n=(e.rows||[]).some(s=>s.corner),a=(e.rows||[]).map(s=>n?[s.state,`<strong>${s.corner}</strong>`,`<code>${s.figma}</code>`,s.extra||""]:[s.state,s.detail||""]);return r("comportamiento","Comportamiento",{lead:e.lead})+(a.length?h(n?["Estado","Radio","Token Figma","Escala"]:["Estado","Qué ocurre"],a):"")+((t=e.notes)!=null&&t.length?`<h3 class="subsection-title">Detalles</h3>${v(e.notes)}`:"")+((o=e.exceptions)!=null&&o.length?`<h3 class="subsection-title">${e.exceptionsTitle||"Qué no lleva morph"}</h3>${v(e.exceptions)}`:"")+"</section>"}function _e(e){return e!=null&&e.length?r("accesibilidad","Accesibilidad",{lead:"Se comprueba en el gate <code>a11yCheck</code> antes de cerrar la pantalla."})+v(e)+"</section>":""}function ke(e){if(!e)return"";const n=[["Color","color",e.color],["Forma","shape",e.shape],["Espaciado","spacing",e.spacing],["Tipografía","type",e.type]].filter(([,,o])=>o==null?void 0:o.length);if(!n.length)return"";const a=o=>o?`<span class="tok-swatch" style="background:${o}"></span><code>${o}</code>`:"—",t=(o,s)=>o.map(i=>`<div class="tok-color">${o.length>1?`<span class="tok-role">${i.role}</span>`:""}${a(i[s])}</div>`).join("");return r("tokens","Tokens y roles",{lead:"Las variables del tema que usa el componente y lo que contienen, leídas del código. Si hace falta un valor nuevo, se crea una variable en el tema: el componente no lleva valores sueltos.",generated:!0})+n.map(([o,s,i])=>s==="color"?`<h3 class="subsection-title">${o}</h3>${h(["Variable","Claro","Oscuro","Dónde se aplica"],i.map(l=>{const d=Y(l.token);return[`<code>${l.token}</code>`,t(d,"light"),t(d,"dark"),l.use]}))}`:`<h3 class="subsection-title">${o}</h3>${h(["Variable","Qué contiene","Dónde se aplica"],i.map(l=>[l.variable===!1?"<em>Sin variable</em>":`<code>${l.token}</code>`,l.value||Q(s,l.token)||"—",l.use]))}`).join("")+"</section>"}function ne(e){const n=e.sourceFile||"",a=[],t={material3:"Material 3, sin componente propio",designsystem:"Design system de MyOwnTrip",app:"La propia app (todavía fuera del design system)","material3 + designsystem":"Material 3, con botones del design system"}[e.module];if(t&&a.push(["Dónde está",t]),n&&!n.startsWith("androidx.")){const o=n.split("/").pop(),s=e.defines||n.startsWith("designsystem/")||n.includes("/ui/components/");a.push([s?"Archivo":"Ejemplo de uso",`<code title="${n}">${o}</code>`])}return a.length?`<dl class="impl-facts">${a.map(([o,s])=>`<dt>${o}</dt><dd>${s}</dd>`).join("")}</dl>`:""}function ze(e){if(!e)return"";const n=(e.params||[]).map(a=>[`<code>${a.name}</code>`,`<code>${y(a.type)}</code>`,a.default==="—"?"—":`<code>${y(a.default)}</code>`,a.note||""]);return r("implementacion","Implementación",{lead:e.note||"",generated:!0})+ne(e)+(e.import?`<pre><code>import ${y(e.import)}</code></pre>`:"")+(n.length?`<h3 class="subsection-title">Parámetros de <code>${e.composable}</code></h3>
         ${h(["Parámetro","Tipo","Por defecto","Nota"],n)}`:"")+(e.snippet?`<h3 class="subsection-title">Uso típico</h3><pre><code>${y(e.snippet)}</code></pre>`:"")+(e.variantsSnippet?`<h3 class="subsection-title">Otras variantes</h3><pre><code>${y(e.variantsSnippet)}</code></pre>`:"")+"</section>"}function He(e){var t;const n=e.decisions||{},a=(n.adr||[]).map(o=>`<li><code>${o.ref}</code> — ${o.label}</li>`).join("");return r("decisiones","Decisiones")+`<dl class="decisions__list">
      <dt>Fuente</dt><dd>${x(n.source)}</dd>
      <dt>Por qué</dt><dd>${x(n.why)}</dd>
      <dt>Capa</dt><dd>${x(n.layer)}</dd>
    </dl>
    ${a?`<h3 class="subsection-title">ADR</h3><ul class="list-dos">${a}</ul>`:""}
    ${(t=e.changelog)!=null&&t.length?`<h3 class="subsection-title">Changelog</h3>${h(["Fecha","Nota"],e.changelog.map(o=>[o.date,o.note]))}`:""}</section>`}function Ae(e){return e!=null&&e.length?r("relacionados","Relacionados")+`<div class="related-row">${e.map(n=>`<a class="related-link" href="${n.href}">${n.label}</a>`).join("")}</div></section>`:""}const te=25e3;let T=!1;const oe=e=>Object.entries(e).map(([n,a])=>`data-p-${n}="${String(a).replace(/"/g,"&quot;")}"`).join(" ");function L(e,n={},{height:a=168,label:t="Demo viva"}={}){return`
    <div class="live-host" role="group" aria-label="${t}" style="height:${a}px"
         data-mot-specimen="${e}" data-mot-base="${V("live/")}" ${oe(n)}>
      <p class="live-host__msg">Cargando el componente real de Compose…</p>
    </div>`}function je(e,n){e&&(Object.entries(n).forEach(([a,t])=>e.setAttribute(`data-p-${a}`,String(t))),e.dispatchEvent(new Event("mot:props")))}function _(e){if(T)return;T=!0;const n=document.createElement("script");n.src=V("live/demos.js"),n.onerror=()=>k(e,"No se encuentra el bundle de demos vivas (public/live/)."),document.head.appendChild(n),setTimeout(()=>{e.forEach(a=>{a.hasAttribute("data-mot-ready")||k([a],"No se ha podido cargar la demo. Necesita un navegador reciente con WebAssembly GC (Chrome, Edge, Firefox o Safari actuales).")})},te)}function k(e,n){e.forEach(a=>{const t=a.querySelector(".live-host__msg");t&&(t.textContent=n),a.classList.add("live-host--error")})}function Ie(){const e=[...document.querySelectorAll("[data-mot-specimen]")];if(!e.length)return;if(!("IntersectionObserver"in window))return _(e);const n=new IntersectionObserver(a=>{a.some(t=>t.isIntersecting)&&(n.disconnect(),_(e))},{rootMargin:"200px"});e.forEach(a=>n.observe(a))}const se='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M12 8a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4z"/></svg>',Ve='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M19 4h-1V2h-2v2H8V2H6v2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V10h14v10zm0-12H5V6h14v2z"/></svg>',ie='<svg viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M21 16v-2l-8-5V3.5A1.5 1.5 0 0 0 11.5 2 1.5 1.5 0 0 0 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z"/></svg>',le='<svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/></svg>';function We({title:e="Billete tren",support:n="Transporte · 12 jun 2026",more:a=!0}={}){return`
    <div class="mot-li">
      <span class="mot-li__lead">${ie}</span>
      <span class="mot-li__text"><strong>${e}</strong><span>${n}</span></span>
      ${a?`<span class="mot-li__trail">${se}</span>`:""}
    </div>`}function z({message:e="Gasto guardado",action:n="Deshacer",close:a=!0}={}){return`
    <div class="snackbar-demo">
      <span class="snackbar-demo__text">${e}</span>
      ${n?`<button type="button" class="snackbar-demo__action" tabindex="-1">${n}</button>`:""}
      ${a?`<button type="button" class="snackbar-demo__close" tabindex="-1" aria-label="Cerrar">${le}</button>`:""}
    </div>`}function C(e="Próximo viaje",n="tertiary",a="md"){return`<span class="eyebrow eyebrow--${n} eyebrow--${a}">${e}</span>`}function H({label:e,value:n="",placeholder:a="",error:t="",readonly:o=!1}){return`
    <label class="mot-field${t?" mot-field--error":""}">
      <span class="mot-field__label">${e}</span>
      <input class="mot-field__input" type="text" value="${n}" placeholder="${a}" ${o?"readonly":""} autocomplete="off" />
      ${t?`<span class="mot-field__support">${t}</span>`:""}
    </label>`}const A="Inter, system-ui, sans-serif",S=(e,n,a,t)=>`<svg class="ad-svg" viewBox="0 0 ${e} ${n}" role="img" aria-label="${t}" xmlns="http://www.w3.org/2000/svg">${a}</svg>`,u=(e,n,a,t,o)=>`<line x1="${n}" y1="${a}" x2="${t}" y2="${o}" class="ad-leader"/><circle cx="${t}" cy="${o}" r="3" class="ad-dot"/><circle cx="${n}" cy="${a}" r="15" class="ad-badge"/><text x="${n}" y="${a+5}" text-anchor="middle" class="ad-badge-text">${e}</text>`,f=(e,n,a,t,o,s="ad-pad")=>`<rect x="${e}" y="${n}" width="${a}" height="${t}" class="${s}"/>`+(o?`<text x="${e+a/2}" y="${n+t/2+4}" text-anchor="middle" class="ad-dim">${o}</text>`:""),B=(e,n,a,t)=>`<path d="M${e+a*.5} ${n+a*.15}v${a*.7}M${e+a*.15} ${n+a*.5}h${a*.7}" stroke="${t}" stroke-width="${Math.max(1.5,a*.08)}" stroke-linecap="square" fill="none"/>`,j='<svg class="mot-btn__icon" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><circle cx="12" cy="12" r="10" fill="currentColor"/><path fill="var(--primary)" d="M12 6.8 13.6 11h4.6l-3.7 2.7 1.4 4.3L12 15.2l-3.9 2.8 1.4-4.3L5.8 11h4.6L12 6.8z"/></svg>',I='<svg class="filter-chip__icon" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M9.55 18 3.85 12.3l1.4-1.4 4.3 4.3 9.2-9.2 1.4 1.4z"/></svg>',de='<svg class="dialog-demo__icon-svg" viewBox="0 0 24 24" width="24" height="24" aria-hidden="true"><path fill="currentColor" d="M12 2 1 21h22L12 2zm1 15h-2v-2h2v2zm0-4h-2V9h2v4z"/></svg>';function ce(e){const n=`
    <div class="ad-live">
      <button type="button" class="mot-btn mot-btn--filled" tabindex="-1">
        ${j}Abrir cuaderno
      </button>
    </div>`,a=(o,s)=>`<span class="spec-n spec-n--${s}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:${e.icon}px">
        <button type="button" class="mot-btn mot-btn--filled spec-ghost" tabindex="-1">${j}Abrir cuaderno</button>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${a(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${a(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
          <span class="spec-band spec-band--gap">${a(e.gap,"gap")}</span>
        </div>
      </div>
    </div>`;return{anatomy:n,measures:t}}function re(e){const a=e.container*3,t=(560-a)/2,o=70,s=e.touch*3,i=o-(s-a)/2,l=t-(s-a)/2,d=e.icon*3,m=t+(a-d)/2,c=o+(a-d)/2,g=`<rect x="${t}" y="${o}" width="${a}" height="${a}" class="ad-fill-surface"/>`+B(m,c,d,"var(--on-surface-variant)"),$=`<rect x="${l}" y="${i}" width="${s}" height="${s}" class="ad-touch"/>`+g+`<rect x="${t+5}" y="${o+5}" width="${a-10}" height="${a-10}" class="ad-layer"/>`+u(1,t-80,o+20,t,o+20)+u(2,t+a+80,o+20,m+d,c+d/2)+u(3,t-80,o+a-20,t+6,o+a-20)+u(4,280,i+s+42,280,i+s),p=(e.container-e.icon)/2*3,F=g+f(t,o,p,a,(e.container-e.icon)/2)+f(t+a-p,o,p,a,(e.container-e.icon)/2)+f(t+p,o,d,p,"","ad-pad ad-pad-v")+f(t+p,o+a-p,d,p,"","ad-pad ad-pad-v")+`<line x1="${t}" y1="${o+a+22}" x2="${t+a}" y2="${o+a+22}" class="ad-line"/><text x="${t+a/2}" y="${o+a+40}" text-anchor="middle" class="ad-dim ad-dim-out">${e.container} dp</text><line x1="${l}" y1="${i-18}" x2="${l+s}" y2="${i-18}" class="ad-line ad-line-touch"/><text x="${l+s/2}" y="${i-26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.touch} dp (táctil)</text>`;return{anatomy:S(560,260,$,"Anatomía del icon button"),measures:S(560,260,F,"Medidas del icon button")}}function pe(e){const s=e.circle*1.5,i=e.padH*1.5,l=e.padV*1.5,d=(e.circle+e.padV*2+8)*1.5,m=10+i+s/2,c=58+d/2,g=10+i+s+e.gap*1.5,$=e.glyph*1.5,p=`<rect x="10" y="58" width="540" height="${d}" class="ad-fill-row"/><rect x="10" y="58" width="4" height="${d}" class="ad-drift"/><circle cx="${m}" cy="${c}" r="${s/2}" class="ad-fill-container"/>`+B(m-$/2,c-$/2,$,"var(--on-surface-variant)")+`<circle cx="${m+s/2-4}" cy="${c-s/2+4}" r="${e.dot*1.5/2}" class="ad-drift-dot"/><text x="${g}" y="${c-4}" font-family="${A}" font-weight="500" font-size="${16*1.5*.8}" fill="var(--on-surface)">Vuelo Lisboa – Madrid</text><text x="${g}" y="${c+18}" font-family="${A}" font-size="${14*1.5*.8}" fill="var(--on-surface-variant)">Vuelo · 14 mar · plan distinto</text><g class="ad-trailing"><circle cx="${550-i-6}" cy="${c}" r="3" fill="var(--on-surface-variant)"/><circle cx="${550-i-6}" cy="${c-9}" r="3" fill="var(--on-surface-variant)"/><circle cx="${550-i-6}" cy="${c+9}" r="3" fill="var(--on-surface-variant)"/></g>`,F=p+u(1,530,32,530,58)+u(2,m,32,m,c-s/2+2)+u(3,g+70,32,g+70,c-18)+u(4,g+150,58+d+34,g+150,c+12)+u(5,480,58+d+34,550-i-6,c+14)+u(6,50,58+d+34,12,58+d-6),N=p+f(14,58,i-4,d,e.padH,"ad-pad")+f(10+i,58,s,l,e.padV,"ad-pad ad-pad-v")+f(10+i,58+d-l,s,l,e.padV,"ad-pad ad-pad-v")+f(10+i+s,c-8,e.gap*1.5,16,"","ad-gap")+`<text x="${10+i+s+e.gap*1.5/2}" y="${58+d+26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.gap}</text><text x="${m}" y="${58+d+26}" text-anchor="middle" class="ad-dim ad-dim-out">${e.circle}</text>`;return{anatomy:S(560,200,F,"Anatomía del list item"),measures:S(560,170,N,"Medidas del list item")}}function ue(e){const a=`
    <div class="ad-live">
      <div class="filter-chip-row">
        ${`<button type="button" class="filter-chip filter-chip--selected" tabindex="-1">${I}Activos</button>`}
        <button type="button" class="filter-chip" tabindex="-1">Archivados</button>
        <button type="button" class="filter-chip" tabindex="-1">Todos</button>
      </div>
    </div>`,t=(s,i)=>`<span class="spec-n spec-n--${i}">${s}</span>`,o=`
    <div class="spec-stage">
      <div class="spec-live spec-live--chip" style="--spec-pad-x:${e.padHSelected}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:${e.icon}px">
        <button type="button" class="filter-chip filter-chip--selected spec-ghost" tabindex="-1">${I}Activos</button>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${t(e.padHSelected,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${t(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
          <span class="spec-band spec-band--gap">${t(e.gap,"gap")}</span>
        </div>
      </div>
    </div>`;return{anatomy:a,measures:o}}function me(e){const n=`
      <div class="dialog-demo__icon">${de}</div>
      <h3 class="dialog-demo__title">¿Eliminar documento?</h3>
      <p class="dialog-demo__body">Se borrará el documento y su archivo del dispositivo. Esta acción no se puede deshacer.</p>
      <div class="dialog-demo__actions">
        <button type="button" class="mot-btn mot-btn--text" tabindex="-1">Cancelar</button>
        <button type="button" class="mot-btn mot-btn--text dialog-demo__destructive" tabindex="-1">Eliminar</button>
      </div>`,a=`<div class="ad-live ad-live--dialog"><div class="dialog-demo">${n}</div></div>`,t=(s,i)=>`<span class="spec-n spec-n--${i}">${s}</span>`,o=`
    <div class="spec-stage spec-stage--dialog">
      <div class="spec-live spec-live--dialog" style="--spec-pad-x:${e.pad}px;--spec-pad-y:${e.pad}px;--spec-gap:${e.actions}px">
        <div class="dialog-demo spec-ghost">${n}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${t(e.pad,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${t(e.pad,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;return{anatomy:a,measures:o}}function ge(e){const n=`<div class="ad-live">${z()}</div>`,a=(o,s)=>`<span class="spec-n spec-n--${s}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live spec-live--snackbar" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px;--spec-gap:${e.gap}px;--spec-icon:0px">
        <div class="spec-ghost">${z()}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${a(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${a(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;return{anatomy:n,measures:t}}function he(e){const n=`<div class="ad-live ad-live--field">${H({label:"Nombre del viaje",value:"Barcelona 2026"})}</div>`,a=(o,s)=>`<span class="spec-n spec-n--${s}">${o}</span>`,t=`
    <div class="spec-stage">
      <div class="spec-live spec-live--field" style="--spec-pad-x:${e.padH}px;--spec-pad-y:${e.padV}px">
        <div class="spec-ghost">${H({label:"Nombre del viaje",value:"Barcelona 2026"})}</div>
        <div class="spec-overlay" aria-hidden="true">
          <span class="spec-band spec-band--pad-l">${a(e.padH,"pad")}</span>
          <span class="spec-band spec-band--pad-t">${a(e.padV,"pad")}</span>
          <span class="spec-band spec-band--pad-b"></span>
          <span class="spec-band spec-band--pad-r"></span>
        </div>
      </div>
    </div>`;return{anatomy:n,measures:t}}function ve(){const e=`<div class="ad-live">
    ${C("Próximo viaje","tertiary","md")}
    ${C("En destino","surface","md")}
    ${C("Recuerdo","secondary","sm")}
  </div>`;return{anatomy:e,measures:e}}const b={button:ce,"icon-button":re,"list-item":pe,"filter-chip":ue,dialog:me,snackbar:ge,"text-field":he,eyebrow:ve};function fe(e,n){const a=`<ol class="list-anatomy ad-legend" id="li-legend">${e.map((t,o)=>{var s;return`<li tabindex="0" data-part="${((s=n.markers[o])==null?void 0:s.part)||""}">${t}</li>`}).join("")}</ol>`;return r("anatomia","Anatomía",{lead:"Las partes del componente y cómo las llamamos en diseño y en código. El dibujo es el componente real de la app: cada línea llega a la pieza que mide Compose. Pasa el cursor por la lista para resaltar cada parte."})+`<div class="ad-layout ad-layout--live">
       <figure class="ad-figure li-wrap" data-parts='${JSON.stringify(O(n))}' data-inspect="anatomy" data-legend="#li-legend" data-markers='${JSON.stringify(n.markers)}'>
         ${L(n.specimen,{zoom:n.zoom,theme:"light",...n.props||{}},{height:n.height,label:"Anatomía del componente real"})}
         <svg class="li-overlay" aria-hidden="true"></svg>
       </figure>
       ${a}
     </div></section>`}function be(e){const n=e.live,a=e.values||{};return r("medidas","Medidas",{lead:"Pasa el cursor sobre el componente: como el inspector del navegador, se resalta cada pieza con su tamaño y sus distancias en dp. En el contenedor, el verde es el espacio interior y el azul, el contenido. Los números se miden en el componente real, no se escriben a mano."})+`<div class="ad-layout ad-layout--live">
       <figure class="ad-figure li-wrap" data-parts='${JSON.stringify(O(n))}' data-inspect="measures" data-table="#li-measures" data-rows='${JSON.stringify(n.rows||null)}' data-gaps='${JSON.stringify(n.gaps||[])}'>
         ${L(n.specimen,{zoom:n.zoom,theme:"light",...n.props||{}},{height:n.height,label:"Medidas del componente real"})}
         <svg class="li-overlay" aria-hidden="true"></svg>
         <div class="li-hit" aria-hidden="true"></div>
       </figure>
       <div>
         <table><thead><tr><th>Medida</th><th>Medido en Compose</th></tr></thead>
           <tbody id="li-measures"><tr><td colspan="2">Midiendo el componente…</td></tr></tbody></table>
         ${a.padH!=null?`<p class="li-note">Padding declarado en código: ${a.padH} × ${a.padV} dp. El espacio arriba y abajo puede ser mayor porque el contenedor tiene una altura mínima y centra el contenido.</p>`:""}
       </div>
     </div></section>`}function O(e){const n=new Set((e.markers||[]).map(a=>a.part));for(const a of e.rows||[])for(const t of["size","left","right","top","gap","vgap"])[].concat(a[t]||[]).forEach(o=>n.add(o));for(const a of e.gaps||[])a.forEach(t=>n.add(t));return[...n]}function Le(e,n){var o;if(!(e!=null&&e.length))return"";if(n!=null&&n.live)return fe(e,n.live);const a=n&&((o=b[n.kind])==null?void 0:o.call(b,n.values)),t=`<ol class="list-anatomy ad-legend">${e.map(s=>`<li>${s}</li>`).join("")}</ol>`;return r("anatomia","Anatomía",{lead:a?"Las partes del componente y cómo las llamamos en diseño y en código.":"Partes que componen el control y cómo se nombran en Figma y en Material 3."})+(a?`<div class="ad-layout"><figure class="ad-figure">${a.anatomy}</figure>${t}</div>`:t)+"</section>"}function Be(e){var a,t;if(e!=null&&e.live)return be(e);const n=e&&((a=b[e.kind])==null?void 0:a.call(b,e.values));return n?r("medidas","Medidas",{lead:"El control se ve en velo; en color, solo padding y huecos (1 px = 1 dp)."})+`<div class="ad-layout"><figure class="ad-figure">${n.measures}</figure>`+((t=e.measures)!=null&&t.length?h(["Medida","Valor"],e.measures.map(o=>[o.label,`<strong>${o.value}</strong>`])):"")+"</div></section>":""}export{Ve as I,j as S,Fe as a,r as b,Ce as c,Me as d,Se as e,we as f,Le as g,Be as h,Te as i,_e as j,ke as k,L as l,ze as m,He as n,Ae as o,Ie as p,We as q,xe as r,je as s,h as t};
