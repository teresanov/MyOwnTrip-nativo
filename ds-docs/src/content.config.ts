import { defineCollection, z } from "astro:content";
import { glob } from "astro/loaders";
import { docsLoader, i18nLoader } from "@astrojs/starlight/loaders";
import { docsSchema, i18nSchema } from "@astrojs/starlight/schema";

/**
 * Dos colecciones:
 *
 * - `docs`      → las paginas escritas en Markdown (foundations, empezar, gobernanza).
 *                 Es la coleccion que Starlight espera y gestiona.
 * - `componentes` → los JSON de ficha, leidos de ds-showcase. No se duplican.
 *
 * El esquema de abajo es el contrato de una ficha. Si falta un campo obligatorio,
 * `npm run build` falla y dice en que archivo: la documentacion incompleta no se publica.
 */

const statusSchema = z.enum(["draft", "in-review", "ready", "deprecated"]);

const componentSchema = z.object({
  // --- Identidad (obligatorio en toda ficha) ---
  id: z.string(),
  name: z.string(),
  status: statusSchema,
  priority: z.string(),
  // Opcional a proposito: si la ficha no la trae, se toma del indice
  // components.json. Un valor por defecto aqui taparia ese dato.
  category: z.string().optional(),
  summary: z.string().optional(),

  // --- Uso ---
  // `usage` es la forma nueva; `purpose` la de las fichas aun sin migrar.
  purpose: z.string().optional(),
  usage: z
    .object({
      purpose: z.string().optional(),
      when: z.array(z.string()).default([]),
      whenNot: z
        .array(
          z.object({
            case: z.string(),
            use: z.string(),
            href: z.string().optional(),
          }),
        )
        .default([]),
      chooseVariant: z
        .array(
          z.object({
            if: z.string(),
            use: z.string(),
            note: z.string().optional(),
          }),
        )
        .default([]),
      related: z
        .array(z.object({ label: z.string(), href: z.string().optional() }))
        .default([]),
    })
    .optional(),

  variants: z
    .array(
      z.object({
        id: z.string(),
        label: z.string(),
        usage: z.string(),
        composable: z.string().optional(),
        figmaNode: z.string().optional(),
      }),
    )
    .default([]),

  // Guia oficial de M3 (si el componente existe en M3) y reglas del equipo.
  guidelines: z
    .object({
      label: z.string().optional(),
      url: z.string().optional(),
      links: z.array(z.object({ label: z.string(), url: z.string() })).default([]),
      consulted: z.string().optional(),
    })
    .optional(),
  dos: z.array(z.string()).default([]),
  donts: z.array(z.string()).default([]),
  productRules: z.object({
    dos: z.array(z.string()).default([]),
    donts: z.array(z.string()).default([]),
  }),

  // --- Anatomia y comportamiento ---
  // Admiten HTML inline (<strong>, <code>) porque son contenido propio.
  anatomy: z.array(z.string()),
  behavior: z
    .object({
      lead: z.string().optional(),
      rows: z
        .array(
          z.object({
            state: z.string(),
            corner: z.string().optional(),
            figma: z.string().optional(),
            extra: z.string().optional(),
          }),
        )
        .default([]),
      notes: z.array(z.string()).default([]),
      exceptions: z.array(z.string()).default([]),
    })
    .optional(),

  // --- Accesibilidad ---
  a11y: z.array(z.string()),

  // --- Codigo ---
  // `tokens` admite la forma agrupada (nueva) y la lista plana (antigua).
  tokens: z
    .union([
      z.array(z.string()),
      z.record(z.string(), z.array(z.object({ token: z.string(), use: z.string() }))),
    ])
    .default([]),
  implementation: z
    .object({
      module: z.string().optional(),
      sourceFile: z.string().optional(),
      import: z.string().optional(),
      composable: z.string().optional(),
      note: z.string().optional(),
      params: z
        .array(
          z.object({
            name: z.string(),
            type: z.string(),
            default: z.string().optional(),
            note: z.string().optional(),
          }),
        )
        .default([]),
      snippet: z.string().optional(),
      variantsSnippet: z.string().optional(),
    })
    .optional(),
  // Forma antigua: snippet suelto y tabla de propiedades.
  compose: z.string().optional(),
  properties: z
    .array(
      z.object({
        name: z.string(),
        type: z.string().optional(),
        example: z.string().optional(),
      }),
    )
    .default([]),

  // --- Meta ---
  figma: z
    .union([
      z.string(),
      z.object({
        label: z.string().optional(),
        node: z.string().optional(),
        url: z.string(),
      }),
      z.null(),
    ])
    .optional(),
  decisions: z.object({
    source: z.string().optional(),
    why: z.string().optional(),
    layer: z.string().optional(),
    adr: z
      .array(z.object({ label: z.string(), ref: z.string().optional() }))
      .default([]),
  }),
  changelog: z.array(z.object({ date: z.string(), note: z.string() })),

  // Campos especificos de alguna ficha; se aceptan sin romper el build.
  productExamples: z.array(z.record(z.string(), z.any())).default([]),
  walletCaps: z.array(z.record(z.string(), z.any())).default([]),
  origin: z.record(z.string(), z.any()).optional(),
  nestedEditable: z.array(z.string()).default([]),
});

export type ComponentData = z.infer<typeof componentSchema>;

export const collections = {
  docs: defineCollection({ loader: docsLoader(), schema: docsSchema() }),
  // Permite traducir los textos de la interfaz de Starlight si hiciera falta.
  i18n: defineCollection({ loader: i18nLoader(), schema: i18nSchema() }),
  componentes: defineCollection({
    loader: glob({
      pattern: "*.json",
      base: "../ds-showcase/src/data/components",
    }),
    schema: componentSchema,
  }),
};
