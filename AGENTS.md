# Agentes — MyOwnTrip nativo

Guía de roles y delegación para Cursor. El producto vive en Compose; este archivo define **cómo** trabaja el agente de paridad DS.

---

## Rol principal: **DS Parity** (paridad Compose ↔ Figma)

| | |
|--|--|
| **Activación** | Pedir paridad Figma, caps design-file, export tokens, bindings, o citar skill `myowntrip-ds-parity` / regla `ds-parity-agent` |
| **Skill** | [`.cursor/skills/myowntrip-ds-parity/SKILL.md`](.cursor/skills/myowntrip-ds-parity/SKILL.md) |
| **Regla** | [`.cursor/rules/ds-parity-agent.mdc`](.cursor/rules/ds-parity-agent.mdc) |
| **Bitácora** | [`docs/design-system/parity-log.md`](docs/design-system/parity-log.md) — único append de cierres |
| **Referencia commit** | `main` @ `6bf4f5f` (nov 2026): cola pendiente vacía |

### Siempre aplican (portero visual)

- [`figma-instances-only.mdc`](.cursor/rules/figma-instances-only.mdc)
- [`m3-native-ui.mdc`](.cursor/rules/m3-native-ui.mdc)
- [`ds-binding-governance.mdc`](.cursor/rules/utilities/ds-binding-governance.mdc)
- [`icon-color-consumption.mdc`](.cursor/rules/utilities/icon-color-consumption.mdc)

### Otros roles (no sustituyen DS Parity)

| Rol | Skill / regla | Cuándo |
|-----|----------------|--------|
| **Producto / features** | `myowntrip-context`, `myowntrip-development.mdc` | Implementar JTBD, Room, pantallas nuevas en Kotlin |
| **Auditoría tokens/Kotlin** | `myowntrip-ds-audit` | Tras export Figma, drift `Color.kt`, gate m3Canonical en código |
| **Pipeline semántico Figma** | `ds-semantic-pipeline` | Crear colecciones/alias de variables en Figma (Modo B) |
| **UX investigación** | `myowntrip-ux-notion` | Persona, hipótesis, Notion — no paridad visual |

---

## Subagentes (Task / delegate)

Usar subagentes para **reducir búsqueda**, no para cerrar Figma sin orquestación del rol principal.

| Tipo | Modelo | Uso en DS Parity |
|------|--------|------------------|
| `explore` | inherit | Mapear composables ↔ cap Figma; buscar scripts en `scripts/` |
| `generalPurpose` | inherit | Informe multi-caps (04–08) antes de una tanda MCP |
| `bugbot` | — | Solo si el usuario pide review explícita |
| `ci-investigator` | — | Checks CI en PRs de DS — fuera del flujo parity habitual |

**Herramientas MCP (no subagentes):** `figma-console`, `figwright` — edición y auditoría en archivos `zrGAL4v6` / `Vf2tNMXy`.

### Plantilla de brief (explore)

```text
Full Repository Path: C:\Users\Usuario\AndroidProjects\MOT_nativo
Ámbito: [cap Figma / módulo 0N]
Objetivo: listar controles visibles → composable + línea; contrastar figma-compose-bindings
Salida: tabla control | Compose | ¿fila bindings? | gap DS
No editar archivos; no Figma.
```

### Plantilla de brief (generalPurpose — informe)

```text
Full Repository Path: C:\Users\Usuario\AndroidProjects\MOT_nativo
Ámbito: [ej. Wallet caps 4–4g]
Leer parity-log Pendiente + bindings sección Wallet
Entregar: checklist gates (instancias, mot/spacing, icon/*) por cap con node-id
El agente padre ejecutará cambios Figma y parity-log.
```

---

## Lectura fija (agente DS Parity)

1. [`design-pipeline.md`](docs/design-system/governance/design-pipeline.md)
2. [`figma-compose-bindings.md`](docs/design-system/figma-compose-bindings.md)
3. [`parity-log.md`](docs/design-system/parity-log.md)
4. [`audits/README.md`](docs/design-system/audits/README.md)

---

## Prohibiciones globales (este rol)

- Nuevos `figma-*-2026-*.md` por discrepancia
- Commits/push sin petición
- Producto (p. ej. event bookmark) mezclado con paridad DS
- Reabrir LEGACY Semantic Layout (R5)