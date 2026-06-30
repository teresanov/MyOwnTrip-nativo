# Diario · Spread creativo — spec de diseño

Referencia para Figma (`05 · Día · Spread`) y Compose. North Star: cuaderno Archer & Olive digital, no listado de developer.

## Principios anti-listado

1. Nunca columna de cards idénticas con mismo padding e imagen fija.
2. Piezas con identidad propia: foto dominante, bookmark con chip de evento, boceto en marco, audio burbuja, sticker con sombra.
3. Fraunces en cabecera de día; Inter en captions; sin labels de formulario en captura.
4. Espaciado editorial con offsets sutiles (±1.5°) y bias horizontal alterno.
5. Captura invisible — sheet «Añadir a la página», no «Nueva nota».
6. Empty state evocador — hoja en blanco, no instrucciones técnicas.

## Piezas del spread

| Tipo | Composable | Datos |
|------|------------|-------|
| Foto | `PhotoSpreadPiece` | `pieceType=PHOTO`, `photoUri` |
| Texto | `TextSpreadPiece` | `pieceType=TEXT` |
| Voz | `AudioSpreadPiece` | `pieceType=AUDIO`, `audioUri` |
| Bookmark evento | `EventBookmarkPiece` | `pieceType=EVENT_BOOKMARK`, `planBlockId` |
| Boceto | `SketchSpreadPiece` | `pieceType=SKETCH`, `sketchUri` |
| Sticker | `StickerSpreadPiece` | `pieceType=STICKER`, `stickerUri` |

## Prompts Figma Agent (Config 2026)

Spread mixto:

> Mobile 360×800, travel journal day spread. Warm paper background. Editorial layout — NOT a vertical list of identical cards. Mixed pieces: large photo with slight rotation, event bookmark chip "Museo 11:00", compact audio bubble, square sketch block, small sticker. Fraunces headline for day title. Archer & Olive bullet journal feel.

Sheet de captura:

> Bottom sheet, 6 actions: Photo, Audio, Write, Sketch, Mark event, Sticker. Minimal, no form fields. Material 3 Android.

## Prompts de validación

> ¿Parece listado de developer o cuaderno creativo? ¿Qué lo hace genérico?
