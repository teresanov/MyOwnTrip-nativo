/**
 * Valores de las variables del tema, leídos del código del design system en el build
 * (no se escriben a mano): color (tokens.json, claro y oscuro), espaciado (MOTSpacing.kt),
 * forma (Shapes.kt) y tipografía (Type.skia.kt, la misma escala que Android).
 */
import tokens from "../data/tokens.json";
import spacingKt from "../../../designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/theme/MOTSpacing.kt?raw";
import shapesKt from "../../../designsystem/src/commonMain/kotlin/com/myowntrip/app/ui/theme/Shapes.kt?raw";
import typeKt from "../../../designsystem/src/skiaMain/kotlin/com/myowntrip/app/ui/theme/Type.skia.kt?raw";

/** `MOTSpacing.Tab.horizontal` → "16 dp" (anida los `object`). */
function parseSpacing(src) {
  const out = {};
  const stack = [];
  for (const raw of src.split(/\r?\n/)) {
    const line = raw.trim();
    const obj = line.match(/^object (\w+)\s*\{/);
    if (obj) {
      stack.push(obj[1]);
      continue;
    }
    const v = line.match(/^val (\w+)\s*=\s*([\d.]+)\.dp/);
    if (v) out[[...stack, v[1]].join(".")] = `${v[2]} dp`;
    if (line === "}") stack.pop();
  }
  return out;
}

/** `shapes.small` → "8 dp". */
function parseShapes(src) {
  const out = {};
  for (const m of src.matchAll(/(\w+)\s*=\s*RoundedCornerShape\(([\d.]+)\.dp\)/g)) out[`shapes.${m[1]}`] = `radio ${m[2]} dp`;
  return out;
}

/** `labelMedium` → "Inter Medium · 12 / 16 sp". */
function parseType(src) {
  const out = {};
  const weights = { Normal: "Regular", Medium: "Medium", SemiBold: "SemiBold", Bold: "Bold" };
  for (const m of src.matchAll(/(\w+) = TextStyle\(fontFamily = (\w+)Family, fontWeight = FontWeight\.(\w+), fontSize = ([\d.]+)\.sp, lineHeight = ([\d.]+)\.sp/g)) {
    out[m[1]] = `${m[2]} ${weights[m[3]] || m[3]} · ${m[4]} / ${m[5]} sp`;
  }
  return out;
}

const SPACING = parseSpacing(spacingKt);
const SHAPES = parseShapes(shapesKt);
const TYPE = parseType(typeKt);

/** Valor de una variable de espaciado, forma o tipografía; null si no es una variable conocida. */
export function tokenValue(kind, token) {
  const t = token.trim();
  if (kind === "spacing") return SPACING[t] || null;
  if (kind === "shape") return SHAPES[t] || null;
  if (kind === "type") return TYPE[t] || null;
  return null;
}

/** Roles de color de una celda ("surface / onSurface") con su hex en claro y oscuro. */
export function colorValues(token) {
  return token
    .split("/")
    .map((s) => s.replace(/\(.*?\)/g, "").trim())
    .filter(Boolean)
    .map((role) => {
      const ext = tokens.extensions?.[role];
      return {
        role,
        light: ext?.light || tokens.light?.[role] || null,
        dark: ext?.dark || tokens.dark?.[role] || null,
      };
    });
}
