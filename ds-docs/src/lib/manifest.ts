import manifest from "../../../ds-showcase/src/data/components.json";

/**
 * El indice `components.json` guarda la categoria, el resumen y el orden de
 * presentacion de cada componente. Algunas fichas todavia no repiten esos datos
 * dentro de su propio JSON, asi que se completan desde aqui en lugar de
 * duplicarlos a mano en nueve archivos.
 */

type Entrada = {
  id: string;
  name?: string;
  category?: string;
  summary?: string;
  status?: string;
  priority?: string;
};

const entradas = (manifest as { components: Entrada[] }).components ?? [];

export const manifestById: Record<string, Entrada> = Object.fromEntries(
  entradas.map((e) => [e.id, e]),
);

/** Orden curado del indice. Lo que no aparece en el manifiesto va al final. */
export const ordenById: Record<string, number> = Object.fromEntries(
  entradas.map((e, i) => [e.id, i]),
);

export function ordenDe(id: string): number {
  return ordenById[id] ?? Number.MAX_SAFE_INTEGER;
}
