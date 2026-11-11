#!/usr/bin/env node
/**
 * Genera el contexto del design system para agentes, desde los mismos JSON
 * que alimentan la web. Se ejecuta en cada build (script `prebuild`).
 *
 * Salidas, en `public/` (Vite las copia a `dist/`):
 *   - llms.txt        índice del sistema con enlaces, convención llms.txt
 *   - ds-context.md   uso, composables y prohibiciones, en texto plano
 *
 * El objetivo es que `.cursor/rules/*.mdc` no repita a mano lo que ya está aquí:
 * una regla corta que apunte a ds-context.md no se desfasa.
 */

import { readdir, readFile, writeFile, mkdir } from "node:fs/promises";
import { dirname, join, resolve } from "node:path";
import { fileURLToPath } from "node:url";

const here = dirname(fileURLToPath(import.meta.url));
const root = resolve(here, "..");
const dataDir = join(root, "src", "data");
const outDir = join(root, "public");

const readJson = async (...p) => JSON.parse(await readFile(join(dataDir, ...p), "utf8"));

const bullets = (items) => items.map((i) => `- ${strip(i)}`).join("\n");

/** El JSON lleva algo de HTML para la web; el agente lo quiere en texto plano. */
function strip(s) {
  return String(s)
    .replace(/<\/?(strong|em|code|a|span)[^>]*>/g, "")
    .replace(/<[^>]+>/g, "")
    .replace(/&amp;/g, "&")
    .replace(/&lt;/g, "<")
    .replace(/&gt;/g, ">")
    .replace(/\s+/g, " ")
    .trim();
}

async function loadComponents() {
  const manifest = await readJson("components.json");
  const files = await readdir(join(dataDir, "components"));
  const byId = new Map();
  for (const f of files.filter((f) => f.endsWith(".json"))) {
    const data = JSON.parse(await readFile(join(dataDir, "components", f), "utf8"));
    byId.set(data.id, data);
  }
  return manifest.components.map((entry) => ({ entry, data: byId.get(entry.id) || null }));
}

function componentBlock({ entry, data }) {
  const lines = [`## ${entry.name}`, ""];
  lines.push(`- Estado: ${entry.status}${entry.priority ? ` · ${entry.priority}` : ""}`);
  lines.push(`- Para qué: ${strip(data?.usage?.purpose || data?.purpose || entry.summary)}`);

  const impl = data?.implementation;
  if (impl?.composable) {
    lines.push(`- Composable: \`${impl.composable}\` — \`${impl.import || "?"}\``);
    if (impl.sourceFile) lines.push(`- Fuente: \`${impl.sourceFile}\``);
  } else {
    lines.push(`- Composable: sin documentar en la ficha`);
  }

  if (data?.variants?.length) {
    lines.push("", "### Variantes", "");
    for (const v of data.variants) {
      lines.push(`- **${v.label}**${v.composable ? ` → \`${v.composable}\`` : ""}: ${strip(v.usage)}`);
    }
  }

  if (data?.usage?.when?.length) {
    lines.push("", "### Cuándo usarlo", "", bullets(data.usage.when));
  }

  if (data?.usage?.whenNot?.length) {
    lines.push("", "### Cuándo NO usarlo", "");
    for (const w of data.usage.whenNot) {
      lines.push(`- ${strip(w.case)} → ${strip(w.use)}`);
    }
  }

  if (data?.usage?.chooseVariant?.length) {
    lines.push("", "### Elegir variante", "");
    for (const c of data.usage.chooseVariant) {
      lines.push(`- ${strip(c.if)} → \`${c.use}\`${c.note ? ` — ${strip(c.note)}` : ""}`);
    }
  }

  const donts = [...(data?.donts || []), ...(data?.productRules?.donts || [])];
  if (donts.length) {
    lines.push("", "### Prohibido en este componente", "", bullets(donts));
  }

  if (data?.a11y?.length) {
    lines.push("", "### Accesibilidad", "", bullets(data.a11y));
  }

  if (impl?.snippet) {
    lines.push("", "### Uso típico", "", "```kotlin", impl.snippet, "```");
  }

  return lines.join("\n");
}

async function main() {
  const rules = await readJson("rules.json");
  const tokens = await readJson("tokens.json");
  const site = await readJson("site.json");
  const components = await loadComponents();
  const today = new Date().toISOString().slice(0, 10);

  /* ---------- ds-context.md ---------- */

  const documented = components.filter((c) => c.data);
  const undocumented = components.filter((c) => !c.data);

  const ctx = [
    "# MyOwnTrip · Design System — contexto para agentes",
    "",
    `> Generado el ${today} desde \`ds-showcase/src/data/\`. **No editar a mano:** los cambios se pierden en el siguiente build.`,
    "> Fuente de verdad del comportamiento: el código de \`designsystem/\` y \`app/\`.",
    "",
    "## Principios",
    "",
    bullets(rules.principles),
    "",
    "## Obligatorio",
    "",
    bullets(rules.required),
    "",
    "## Prohibido",
    "",
    bullets(rules.forbidden),
    "",
    "## Roles de color clave",
    "",
    "| Rol | Light | Nota |",
    "| --- | --- | --- |",
    ...rules.keyColors.map((c) => `| \`${c.role}\` | \`${c.light}\` | ${c.note} |`),
    "",
    "## State layers (runtime, no tokens)",
    "",
    Object.entries(tokens.stateLayers)
      .filter(([k]) => k !== "note")
      .map(([k, v]) => `- ${k}: ${v}`)
      .join("\n"),
    "",
    "## Gates",
    "",
    ...rules.gates.map(
      (g) => `- \`${g.id}\`${g.blocking ? " **(bloqueante)**" : ""} — ${g.when}`
    ),
    "",
    "## Componentes documentados",
    "",
    ...documented.map((c) => componentBlock(c) + "\n"),
  ];

  if (undocumented.length) {
    ctx.push(
      "## Componentes sin ficha",
      "",
      "Existen en Figma o en código, pero nadie ha escrito cuándo usarlos.",
      "No inventes criterios de uso para estos: pregunta o consulta el código.",
      "",
      ...undocumented.map((c) => `- **${c.entry.name}** (${c.entry.status}): ${c.entry.summary}`),
      ""
    );
  }

  /* ---------- llms.txt ---------- */

  const base = rules.site.replace(/\/$/, "");
  const navLinks = site.nav.flatMap((n) =>
    n.children
      ? n.children.map((c) => `- [${c.label}](${base}/${c.href})`)
      : n.href
        ? [`- [${n.label}](${base}/${n.href})`]
        : []
  );

  const llms = [
    "# MyOwnTrip Design System",
    "",
    "> Design system de la app MyOwnTrip (Android, Jetpack Compose). Material 3 canónico con identidad editorial: Fraunces y Inter, paleta gris-azul con acento ocre, y botones que pasan de 0 a 20 dp de radio al interactuar.",
    "",
    `Contexto completo en texto plano: [ds-context.md](${base}/ds-context.md)`,
    "",
    "## Secciones",
    "",
    navLinks.join("\n"),
    "",
    "## Componentes",
    "",
    components
      .filter((c) => c.entry.href !== "components.html")
      .map((c) => `- [${c.entry.name}](${base}/${c.entry.href}): ${c.entry.summary}`)
      .join("\n"),
    "",
  ];

  await mkdir(outDir, { recursive: true });
  await writeFile(join(outDir, "ds-context.md"), ctx.join("\n"), "utf8");
  await writeFile(join(outDir, "llms.txt"), llms.join("\n"), "utf8");

  console.log(
    `Contexto de agente generado · ${documented.length} fichas, ${undocumented.length} sin ficha → public/ds-context.md, public/llms.txt`
  );
}

main().catch((err) => {
  console.error("No se pudo generar el contexto de agente:", err);
  process.exit(1);
});
