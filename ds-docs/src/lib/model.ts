/**
 * Normaliza una ficha a un solo modelo.
 *
 * Las fichas existen en dos formas: la nueva (`usage`, `behavior`, `implementation`)
 * y la antigua (`purpose`, `compose`, `properties`). En lugar de migrarlas todas de
 * golpe, aqui se traducen a una sola estructura, de modo que la plantilla no tenga
 * condicionales y las fichas se puedan ir migrando de una en una.
 */

import { manifestById } from "./manifest";
// @ts-ignore -- .mjs compartido con astro.config.mjs, que no puede importar TS.
import { ESTADOS } from "./vocabulario.mjs";

export type FigmaLink = { url: string; label: string; node?: string } | null;

export type TokenGroup = {
  label: string;
  items: { token: string; use: string }[];
};

const TOKEN_GROUP_LABELS: Record<string, string> = {
  color: "Color",
  shape: "Forma",
  spacing: "Espaciado",
  type: "Tipografía",
  motion: "Movimiento",
};

// El vocabulario de estados vive en vocabulario.mjs, compartido con la
// configuracion del menu. Aqui solo se reexporta con el nombre que usan las paginas.
export const STATUS_LABELS: Record<string, string> = ESTADOS;

export function statusBadge(status: string) {
  const variant =
    status === "ready"
      ? "success"
      : status === "deprecated"
        ? "danger"
        : status === "in-review"
          ? "caution"
          : "note";
  return { text: STATUS_LABELS[status] ?? status, variant };
}

function normalizeFigma(figma: unknown): FigmaLink {
  if (!figma) return null;
  if (typeof figma === "string") {
    return { url: figma, label: "Ver en Figma" };
  }
  const f = figma as { url?: string; label?: string; node?: string };
  if (!f.url) return null;
  return { url: f.url, label: f.label ?? "Ver en Figma", node: f.node };
}

function normalizeTokens(tokens: unknown): TokenGroup[] {
  if (!tokens) return [];
  // Forma antigua: lista plana de nombres de rol.
  if (Array.isArray(tokens)) {
    if (tokens.length === 0) return [];
    return [
      {
        label: "Roles M3",
        items: (tokens as string[]).map((t) => ({ token: t, use: "" })),
      },
    ];
  }
  // Forma nueva: agrupados por tipo de token, con el porque de cada uno.
  return Object.entries(tokens as Record<string, { token: string; use: string }[]>)
    .filter(([, items]) => Array.isArray(items) && items.length > 0)
    .map(([key, items]) => ({
      label: TOKEN_GROUP_LABELS[key] ?? key,
      items,
    }));
}

export function normalize(data: any) {
  const impl = data.implementation ?? {};
  // Categoria y resumen pueden venir del indice si la ficha aun no los repite.
  const delIndice = manifestById[data.id] ?? {};

  // Tabla de parametros: la nueva trae tipo y valor por defecto; la antigua, un ejemplo.
  const params =
    impl.params?.length > 0
      ? impl.params
      : (data.properties ?? []).map((p: any) => ({
          name: p.name,
          type: p.type ?? "—",
          default: "—",
          note: p.example ?? "",
        }));

  return {
    id: data.id,
    name: data.name,
    status: data.status,
    priority: data.priority,
    category: data.category ?? delIndice.category ?? "otros",

    // Uso
    summary: data.summary ?? delIndice.summary ?? "",
    purpose: data.usage?.purpose ?? data.purpose ?? "",
    when: data.usage?.when ?? [],
    whenNot: data.usage?.whenNot ?? [],
    chooseVariant: data.usage?.chooseVariant ?? [],
    related: data.usage?.related ?? [],
    variants: data.variants ?? [],
    guidelines: data.guidelines,
    guidelineLinks:
      data.guidelines?.links?.length > 0
        ? data.guidelines.links
        : data.guidelines?.url
          ? [{ label: data.guidelines.label ?? "Material Design 3", url: data.guidelines.url }]
          : [],
    dos: data.dos ?? [],
    donts: data.donts ?? [],
    productRules: data.productRules ?? { dos: [], donts: [] },

    // Anatomia y comportamiento
    anatomy: data.anatomy ?? [],
    behavior: data.behavior,

    // Accesibilidad
    a11y: data.a11y ?? [],

    // Codigo
    tokenGroups: normalizeTokens(data.tokens),
    implementation: impl,
    params,
    snippet: impl.snippet ?? data.compose ?? "",
    variantsSnippet: impl.variantsSnippet ?? "",

    // Meta
    figma: normalizeFigma(data.figma),
    decisions: data.decisions ?? {},
    changelog: data.changelog ?? [],
    productExamples: data.productExamples ?? [],

    /**
     * Una ficha es "antigua" si todavia no tiene el bloque `usage`.
     * Se marca en la pagina para que se vea que falta migrarla, en lugar
     * de que parezca completa.
     */
    isLegacy: !data.usage,
  };
}

export type Ficha = ReturnType<typeof normalize>;
