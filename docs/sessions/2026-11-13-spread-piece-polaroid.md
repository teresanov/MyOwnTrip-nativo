# Follow-up — Spread Piece polaroid + tirita (2026-11-13)

Hecho 2026-11-13 (DS): `Spread piece` Type=Photo = polaroid 150 + `Spread/Washi tape`. Publicar librería para que las caps de Diario actualicen instancias.

Pendiente original: **Compose es SoT**; no aproximar con cards genéricas.

## Qué falta en Figma

El `Spread piece` de Figma no replica el marco tipo polaroid ni la tirita de washi. Hay que buscar (o crear en el DS con átomos, no frames sueltos en el design-file) esos dos elementos y componer la pieza **PHOTO** con ellos.

## SoT Compose

Archivo: `app/src/main/java/com/myowntrip/app/ui/features/journal/spread/ScrapbookAlbum.kt`

`ScrapPieceContent` → `JournalPieceType.PHOTO` → composable **`Polaroid`**.

Anatomía (no es una card M3):

1. **Marco polaroid** — `Column` 150 dp, `Print` `#FCFBF7`, `RoundedCornerShape(2.dp)`, sombra 7 dp. Padding 8 / 8 / 8 / **26** (borde inferior ancho). Foto 112 dp crop, radio 1 dp. Caption opcional: `HandFamily` itálica 16, centrada.
2. **Tirita** — `Box` 54×18 dp, offset (−8, −6), rotación **−32°**, fill `BrandColors.AccentOcre` @ 42 % alfa. Va **encima** del marco, esquina superior izquierda.

Instancias en caps 5 / 5c / sheets: sustituir el `Spread piece` tipo Photo “plano” por esta composición.

Otros tipos (sticker, post-it, mano, audio, sketch) no son polaroid; no copiar la tirita ahí.
