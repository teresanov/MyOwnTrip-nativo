# MyOwnTrip — Requisitos MoSCoW

Alineado con research UX (abril 2026), pain points PP1–PP13 y priorización Must/Should/Could del skill **`myowntrip-ux-notion`**.

**Notion — North Star:** [North Star — El cuaderno del viaje](https://www.notion.so/33e6a48d93c881d8b9cce1418607f3d7).

**Notion (alcance MVP 1.0 y tabla MoSCoW):** [MVP 1.0 — Features (MoSCoW)](https://www.notion.so/33e6a48d93c8813ea599f53ce21ff2b0) dentro de *MyOwnTrip · UX Research*.

## Must Have — Imprescindibles para el MVP

- Crear viajes con nombre, destino y fechas
- **Wallet por viaje:** entradas (vuelo, hotel, actividad, transporte, genérico), PDFs adjuntos y enlaces — **flujo manual** con verificación por el usuario (sin depender de auto-import desde email como camino principal)
- **Itinerario por día** con drag & drop para reordenar bloques
- **Notas de diario** libres por día
- **Multimedia en notas:** fotos, audio y dibujos
- **Cifrado y privacidad:** Face ID, Touch ID o código
- **Modo offline-first:** lectura y edición esenciales sin red; datos **locales como fuente de verdad**; sincronización con Supabase al reconectar **sin sustituir** la confianza en copia local (mitigar PP2, PP3)
- **Supabase** para sync, Storage y backup — el viaje debe seguir siendo usable si el sync falla temporalmente

## Should Have — Debería tener

- **Control de recibos y gastos** por categoría — flujo **muy corto** (objetivo ≤3 interacciones principales; validar H5)
- **Geolocalización** asociada a fotos / notas cuando aplique
- **Lista de equipaje** reutilizable por viaje (PP12)
- Búsqueda global dentro del viaje
- Vista de línea temporal del itinerario completo
- Enlaces externos en entradas (Maps, web, reserva)
- **Freemium honesto:** offline y acceso a documentos esenciales **no** relegados solo a premium (H9)

## Could Have — Podría tener

- **Restaurantes** con tres estados: sin reserva / reservado / visitado (H3 cuestionado en research — no Must Have)
- Mapa con pins de lugares guardados (Google Maps SDK)
- Exportar el viaje como PDF
- **Export / integración Maps** que preserve orden y contexto (PP10) cuando se implemente export
- Widget Android con el plan del día
- Sugerencias de ciudad con IA *(v1.1)*: solo con señal de frescura de datos y corrección; no en MVP si no hay garantías (H6)

## Won't Have — No en el MVP

- Colaboración multiusuario
- APIs de vuelos o hoteles en tiempo real
- **Auto-import masivo desde correo** como feature central sin control humano explícito (contradice H7 y PP5)
- **IA generativa de recomendaciones** como diferenciador principal sin mecanismo anti-datos obsoletos
