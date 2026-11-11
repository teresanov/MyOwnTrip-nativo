import fs from "node:fs";
import path from "node:path";

/**
 * Lee los metadatos de los .ttf que usa la app: version, nombre y los pesos
 * que la fuente trae con nombre propio.
 *
 * Se leen del archivo porque la version es justo el dato que no se puede
 * escribir a mano: si alguien sustituye el .ttf y la pagina sigue diciendo la
 * version antigua, los diseñadores instalan una fuente distinta de la que
 * compila la app y nadie se entera hasta que algo no cuadra.
 */

const FONT_DIR = path.resolve("../designsystem/src/main/res/font");

const ARCHIVOS = ["fraunces.ttf", "fraunces_italic.ttf", "inter.ttf"];

export type Fuente = {
  archivo: string;
  familia: string;
  nombreCompleto: string;
  version: string;
  variable: boolean;
  ejes: { tag: string; min: number; def: number; max: number }[];
  /** Pesos que la fuente trae con nombre: lo que se ve en el desplegable de Figma. */
  instancias: { nombre: string; peso: number | null }[];
  bytes: number;
};

/** Las tablas de un .ttf, por su etiqueta de 4 letras. */
function leerTablas(buf: Buffer): Record<string, { offset: number; length: number }> {
  const numTables = buf.readUInt16BE(4);
  const tablas: Record<string, { offset: number; length: number }> = {};
  for (let i = 0; i < numTables; i++) {
    const r = 12 + i * 16;
    tablas[buf.toString("ascii", r, r + 4)] = {
      offset: buf.readUInt32BE(r + 8),
      length: buf.readUInt32BE(r + 12),
    };
  }
  return tablas;
}

/** La tabla `name`: los textos de la fuente, indexados por nameID. */
function leerNombres(buf: Buffer, tabla: { offset: number }): Record<number, string> {
  const count = buf.readUInt16BE(tabla.offset + 2);
  const stringOffset = buf.readUInt16BE(tabla.offset + 4);
  const nombres: Record<number, string> = {};

  for (let i = 0; i < count; i++) {
    const r = tabla.offset + 6 + i * 12;
    const platformID = buf.readUInt16BE(r);
    const nameID = buf.readUInt16BE(r + 6);
    const len = buf.readUInt16BE(r + 8);
    const off = buf.readUInt16BE(r + 10);
    const ini = tabla.offset + stringOffset + off;
    const trozo = buf.subarray(ini, ini + len);

    let valor = "";
    if (platformID === 3) {
      // Windows: UTF-16 big endian.
      for (let j = 0; j + 1 < trozo.length; j += 2) valor += String.fromCharCode(trozo.readUInt16BE(j));
    } else {
      valor = trozo.toString("latin1");
    }

    // Se queda el primero de cada id: basta con una lectura legible.
    if (valor && nombres[nameID] === undefined) nombres[nameID] = valor;
  }
  return nombres;
}

/** Numero Fixed 16.16 de las tablas OpenType. */
function fixed(buf: Buffer, off: number): number {
  return buf.readInt32BE(off) / 65536;
}

/** La tabla `fvar`: los ejes variables y las instancias con nombre. */
function leerFvar(buf: Buffer, tabla: { offset: number }, nombres: Record<number, string>) {
  const axesOffset = buf.readUInt16BE(tabla.offset + 4);
  const axisCount = buf.readUInt16BE(tabla.offset + 8);
  const axisSize = buf.readUInt16BE(tabla.offset + 10);
  const instanceCount = buf.readUInt16BE(tabla.offset + 12);
  const instanceSize = buf.readUInt16BE(tabla.offset + 14);

  const ejes = [];
  for (let i = 0; i < axisCount; i++) {
    const a = tabla.offset + axesOffset + i * axisSize;
    ejes.push({
      tag: buf.toString("ascii", a, a + 4),
      min: fixed(buf, a + 4),
      def: fixed(buf, a + 8),
      max: fixed(buf, a + 12),
    });
  }

  const iWght = ejes.findIndex((e) => e.tag === "wght");
  const base = tabla.offset + axesOffset + axisCount * axisSize;
  const instancias = [];
  for (let i = 0; i < instanceCount; i++) {
    const o = base + i * instanceSize;
    const nombre = nombres[buf.readUInt16BE(o)] ?? "—";
    const peso = iWght >= 0 ? fixed(buf, o + 4 + iWght * 4) : null;
    instancias.push({ nombre, peso });
  }

  return { ejes, instancias };
}

export function fuentes(): { lista: Fuente[]; dir: string } {
  const lista: Fuente[] = [];

  for (const archivo of ARCHIVOS) {
    const ruta = path.join(FONT_DIR, archivo);
    if (!fs.existsSync(ruta)) continue;

    try {
      const buf = fs.readFileSync(ruta);
      const tablas = leerTablas(buf);
      const nombres = tablas.name ? leerNombres(buf, tablas.name) : {};
      const fvar = tablas.fvar ? leerFvar(buf, tablas.fvar, nombres) : null;

      lista.push({
        archivo,
        familia: nombres[16] ?? nombres[1] ?? "—",
        nombreCompleto: nombres[4] ?? "—",
        // "Version 1.000;[b76b70a41]" → se deja tal cual, es el dato a citar.
        version: (nombres[5] ?? "—").replace(/^Version\s*/i, ""),
        variable: Boolean(fvar),
        ejes: fvar?.ejes ?? [],
        instancias: (fvar?.instancias ?? []).sort((a, b) => (a.peso ?? 0) - (b.peso ?? 0)),
        bytes: buf.length,
      });
    } catch {
      // Un .ttf ilegible no debe tumbar la web: simplemente no se lista.
    }
  }

  return { lista, dir: "designsystem/src/main/res/font/" };
}
