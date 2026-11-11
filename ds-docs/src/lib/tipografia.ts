import fs from "node:fs";
import path from "node:path";

/**
 * Lee la escala tipografica directamente de Type.kt.
 *
 * La alternativa seria copiar los tamaños a mano en la pagina, y entonces la
 * documentacion diria una cosa y el codigo otra en cuanto alguien tocara un valor.
 * Si el archivo cambia de forma y algun rol deja de encontrarse, la pagina lo dice
 * en lugar de inventarse el numero.
 */

const TYPE_KT = path.resolve(
  "../designsystem/src/main/java/com/myowntrip/app/ui/theme/Type.kt",
);

/** FontWeight.X de Compose → el numero que entiende CSS. */
const PESOS: Record<string, number> = {
  Thin: 100,
  ExtraLight: 200,
  Light: 300,
  Normal: 400,
  Medium: 500,
  SemiBold: 600,
  Bold: 700,
  ExtraBold: 800,
  Black: 900,
};

const NOMBRE_PESO: Record<number, string> = {
  300: "Light",
  400: "Regular",
  500: "Medium",
  600: "SemiBold",
  700: "Bold",
  900: "Black",
};

export type RolTipografico = {
  rol: string;
  familia: "Fraunces" | "Inter" | string;
  peso: number;
  pesoNombre: string;
  tamano: number;
  interlineado: number;
  espaciado: number;
};

export type GrupoTipografico = {
  clave: string;
  label: string;
  descripcion: string;
  roles: RolTipografico[];
};

const GRUPOS: [string, string, string][] = [
  ["display", "Display", "Cifras y titulares de portada. Se usan poco y a propósito."],
  ["headline", "Headline", "Titular de pantalla y de sección."],
  ["title", "Title", "Título de una tarjeta, una fila o un bloque."],
  ["body", "Body", "Texto corrido y descripciones."],
  ["label", "Label", "Etiquetas de controles: botones, chips, pestañas."],
];

function parse(): RolTipografico[] {
  if (!fs.existsSync(TYPE_KT)) return [];
  const src = fs.readFileSync(TYPE_KT, "utf8");

  // Solo el bloque Typography(...): fuera definiciones de familias y comentarios.
  const bloque = src.slice(src.indexOf("Typography("));

  const re =
    /(\w+)\s*=\s*TextStyle\(\s*fontFamily\s*=\s*(\w+)\s*,\s*fontWeight\s*=\s*FontWeight\.(\w+)\s*,\s*fontSize\s*=\s*([\d.]+)\.sp\s*,\s*lineHeight\s*=\s*([\d.]+)\.sp\s*,\s*letterSpacing\s*=\s*\(?(-?[\d.]+)\)?\.sp/g;

  const roles: RolTipografico[] = [];
  for (const m of bloque.matchAll(re)) {
    const peso = PESOS[m[3]] ?? 400;
    roles.push({
      rol: m[1],
      familia: m[2].replace(/Family$/, ""),
      peso,
      pesoNombre: NOMBRE_PESO[peso] ?? String(peso),
      tamano: Number(m[4]),
      interlineado: Number(m[5]),
      espaciado: Number(m[6]),
    });
  }
  return roles;
}

/**
 * Pesos que Type.kt declara al construir cada familia
 * (`frauncesFont(FontWeight.Medium)`, …). Sirve para contrastarlos con los
 * pesos que la fuente trae con nombre propio: si el codigo pide uno que la
 * fuente no nombra, Compose lo interpola pero en Figma no hay estilo que elegir.
 */
export function pesosDeclarados(): Record<string, { nombre: string; peso: number }[]> {
  if (!fs.existsSync(TYPE_KT)) return {};
  const src = fs.readFileSync(TYPE_KT, "utf8");

  const out: Record<string, { nombre: string; peso: number }[]> = {};
  for (const [, fn, peso] of src.matchAll(/(\w+)Font\(FontWeight\.(\w+)\)/g)) {
    const familia = fn.charAt(0).toUpperCase() + fn.slice(1);
    const lista = (out[familia] ??= []);
    if (!lista.some((p) => p.nombre === peso)) {
      lista.push({ nombre: peso, peso: PESOS[peso] ?? 400 });
    }
  }
  for (const k of Object.keys(out)) out[k].sort((a, b) => a.peso - b.peso);
  return out;
}

export function escalaTipografica(): {
  grupos: GrupoTipografico[];
  total: number;
  origen: string;
} {
  const roles = parse();

  const grupos = GRUPOS.map(([clave, label, descripcion]) => ({
    clave,
    label,
    descripcion,
    roles: roles.filter((r) => r.rol.startsWith(clave)),
  })).filter((g) => g.roles.length > 0);

  return {
    grupos,
    total: roles.length,
    origen: "designsystem/src/main/java/com/myowntrip/app/ui/theme/Type.kt",
  };
}
