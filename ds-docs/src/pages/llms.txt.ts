import type { APIRoute } from "astro";
import { getCollection } from "astro:content";
import { normalize, STATUS_LABELS } from "../lib/model";

/**
 * Genera /llms.txt: la misma documentacion en texto plano, para que un agente
 * (Cursor, Claude, el que sea) la lea completa sin rastrear el HTML.
 *
 * Sale de la misma coleccion que las paginas, asi que no puede desincronizarse.
 * Atlassian parte su llms.txt en varios archivos por tamaño; con nueve
 * componentes no hace falta: un archivo basta.
 */

/** Quita el HTML inline de las fichas (<strong>, <code>) para el texto plano. */
function plain(s?: string): string {
  if (!s) return "";
  return s
    .replace(/<[^>]+>/g, "")
    .replace(/\s+/g, " ")
    .trim();
}

function bullets(items: string[]): string {
  return items.map((i) => `- ${plain(i)}`).join("\n");
}

export const GET: APIRoute = async () => {
  const entries = await getCollection("componentes");
  const fichas = entries
    .map((e) => normalize(e.data))
    .sort((a, b) => a.name.localeCompare(b.name, "es"));

  const out: string[] = [];

  out.push("# MyOwnTrip · Design System");
  out.push("");
  out.push(
    "> Design system de la app MyOwnTrip (Android nativo, Jetpack Compose). Material 3 canónico " +
      "con identidad editorial. El código es la fuente de verdad; Figma es la librería visual y " +
      "esta web la documentación.",
  );
  out.push("");
  out.push("## Reglas del sistema");
  out.push("");
  out.push(
    bullets([
      "Consumir siempre MaterialTheme.colorScheme y MaterialTheme.typography; nunca colores literales.",
      "Prohibido crear tokens de color por estado (hover, pressed, focus, disabled): los state layers se aplican en runtime.",
      "Prohibido portar SemanticColors, LayoutTokens ni wrappers del repositorio archivado.",
      "Un error se comunica con icono y texto, nunca solo con color.",
      "Un wrapper MOT* solo existe si M3 no cubre el caso y queda documentado aquí o en un ADR.",
    ]),
  );
  out.push("");
  out.push("## Componentes");
  out.push("");

  for (const f of fichas) {
    out.push(`### ${f.name}`);
    out.push("");
    if (f.summary) out.push(plain(f.summary));
    if (f.purpose) out.push(plain(f.purpose));
    out.push("");

    out.push("**Metadatos**");
    out.push("");
    const meta = [
      `- Id: ${f.id}`,
      `- Estado: ${STATUS_LABELS[f.status] ?? f.status}`,
      `- Prioridad: ${f.priority}`,
      `- Categoría: ${f.category}`,
    ];
    if (f.implementation?.composable) meta.push(`- Composable: ${f.implementation.composable}`);
    if (f.implementation?.sourceFile) meta.push(`- Archivo: ${f.implementation.sourceFile}`);
    if (f.figma?.node) meta.push(`- Nodo de Figma: ${f.figma.node}`);
    if (f.isLegacy) meta.push("- Aviso: ficha en formato antiguo, incompleta.");
    out.push(meta.join("\n"));
    out.push("");

    if (f.when.length > 0) {
      out.push("**Cuándo usarlo**");
      out.push("");
      out.push(bullets(f.when));
      out.push("");
    }

    if (f.whenNot.length > 0) {
      out.push("**Cuándo no usarlo**");
      out.push("");
      out.push(f.whenNot.map((w: any) => `- ${plain(w.case)} → usa ${plain(w.use)}`).join("\n"));
      out.push("");
    }

    if (f.chooseVariant.length > 0) {
      out.push("**Qué variante elegir**");
      out.push("");
      out.push(
        f.chooseVariant
          .map(
            (c: any) =>
              `- Si ${plain(c.if)} → ${plain(c.use)}${c.note ? ` (${plain(c.note)})` : ""}`,
          )
          .join("\n"),
      );
      out.push("");
    }

    if (f.variants.length > 0) {
      out.push("**Variantes**");
      out.push("");
      out.push(
        f.variants
          .map(
            (v: any) =>
              `- ${v.label}${v.composable ? ` (${v.composable})` : ""}: ${plain(v.usage)}`,
          )
          .join("\n"),
      );
      out.push("");
    }

    if (f.anatomy.length > 0) {
      out.push("**Anatomía**");
      out.push("");
      out.push(bullets(f.anatomy));
      out.push("");
    }

    if (f.behavior) {
      out.push("**Comportamiento**");
      out.push("");
      if (f.behavior.lead) out.push(plain(f.behavior.lead));
      if (f.behavior.rows?.length) {
        out.push("");
        out.push(
          f.behavior.rows
            .map(
              (r: any) =>
                `- ${plain(r.state)}: radio ${r.corner ?? "—"}${r.figma ? `, token ${r.figma}` : ""}${r.extra ? `. ${plain(r.extra)}` : ""}`,
            )
            .join("\n"),
        );
      }
      if (f.behavior.notes?.length) {
        out.push("");
        out.push(bullets(f.behavior.notes));
      }
      if (f.behavior.exceptions?.length) {
        out.push("");
        out.push("Excepciones:");
        out.push(bullets(f.behavior.exceptions));
      }
      out.push("");
    }

    if (f.params.length > 0) {
      out.push("**Parámetros**");
      out.push("");
      out.push("| Parámetro | Tipo | Por defecto | Nota |");
      out.push("| --- | --- | --- | --- |");
      out.push(
        f.params
          .map(
            (p: any) =>
              `| ${p.name} | ${p.type} | ${p.default ?? "—"} | ${plain(p.note) || "—"} |`,
          )
          .join("\n"),
      );
      out.push("");
    }

    if (f.snippet) {
      out.push("**Ejemplo**");
      out.push("");
      out.push("```kotlin");
      out.push(f.snippet);
      out.push("```");
      out.push("");
    }

    if (f.tokenGroups.length > 0) {
      out.push("**Tokens que consume**");
      out.push("");
      for (const g of f.tokenGroups) {
        out.push(
          g.items
            .map((i) => `- ${g.label}: ${i.token}${i.use ? ` — ${plain(i.use)}` : ""}`)
            .join("\n"),
        );
      }
      out.push("");
    }

    if (f.dos.length > 0 || f.donts.length > 0) {
      out.push("**Guía de Material Design 3**");
      out.push("");
      if (f.dos.length) out.push(`Do:\n${bullets(f.dos)}`);
      if (f.donts.length) out.push(`\nDon't:\n${bullets(f.donts)}`);
      out.push("");
    }

    if (f.productRules.dos.length > 0 || f.productRules.donts.length > 0) {
      out.push("**Reglas de MyOwnTrip**");
      out.push("");
      if (f.productRules.dos.length) out.push(`Do:\n${bullets(f.productRules.dos)}`);
      if (f.productRules.donts.length) out.push(`\nDon't:\n${bullets(f.productRules.donts)}`);
      out.push("");
    }

    if (f.a11y.length > 0) {
      out.push("**Accesibilidad**");
      out.push("");
      out.push(bullets(f.a11y));
      out.push("");
    }

    if (f.decisions?.why || f.decisions?.layer) {
      out.push("**Procedencia**");
      out.push("");
      const proc: string[] = [];
      if (f.decisions.source) proc.push(`- Fuente: ${plain(f.decisions.source)}`);
      if (f.decisions.why) proc.push(`- Por qué existe: ${plain(f.decisions.why)}`);
      if (f.decisions.layer) proc.push(`- Capa: ${plain(f.decisions.layer)}`);
      for (const a of f.decisions.adr ?? []) {
        proc.push(`- Decisión: ${plain(a.label)}${a.ref ? ` (${a.ref})` : ""}`);
      }
      out.push(proc.join("\n"));
      out.push("");
    }
  }

  return new Response(out.join("\n"), {
    headers: { "Content-Type": "text/plain; charset=utf-8" },
  });
};
