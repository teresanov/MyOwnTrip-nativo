# MyOwnTrip — Modelo de Datos

7 entidades Room. 🔑 = clave primaria, → = clave foránea.

**Prioridad de pantallas MVP:** Trip, Day, WalletEntry, JournalNote (+ Photo, Expense) son núcleo. **Restaurant** permanece en el modelo para no bloquear fases posteriores; en MoSCoW es **Could Have** (research: estados de restaurante no prioridad espontánea en usuarios sintéticos).

## Trip
| Campo | Tipo | |
|---|---|---|
| id | String (UUID) | 🔑 |
| name | String | |
| destination | String | |
| startDate | LocalDate | |
| endDate | LocalDate | |
| coverPhoto | String? (URI) | |
| createdAt | Long | |

## Day
| Campo | Tipo | |
|---|---|---|
| id | String (UUID) | 🔑 |
| tripId | String | → Trip |
| date | LocalDate | |
| dayNumber | Int | |
| title | String? | |

## WalletEntry
| Campo | Tipo | |
|---|---|---|
| id | String (UUID) | 🔑 |
| tripId | String | → Trip |
| type | EntryType | enum |
| title | String | |
| date | LocalDate? | |
| time | LocalTime? | |
| pdfUri | String? | |
| linkUrl | String? | |
| notes | String? | |

*Flujo producto:* entrada creada o importada por el usuario con **revisión explícita**; no asumir corrección automática desde terceros sin confirmación.

## Restaurant
| Campo | Tipo | |
|---|---|---|
| id | String (UUID) | 🔑 |
| tripId | String | → Trip |
| dayId | String? | → Day (opcional) |
| name | String | |
| address | String? | |
| status | Status | enum |
| reservedTime | LocalTime? | |
| confirmCode | String? | |
| notes | String? | |

## JournalNote
| Campo | Tipo | |
|---|---|---|
| id | String (UUID) | 🔑 |
| dayId | String | → Day |
| text | String | |
| audioUri | String? | |
| latitude | Double? | |
| longitude | Double? | |
| createdAt | Long | |

*Research:* útil medir si las notas se crean más en destino que en planificación (hipótesis operativa no reescrita en doc abril 2026).

## Expense
| Campo | Tipo | |
|---|---|---|
| id | String (UUID) | 🔑 |
| tripId | String | → Trip |
| dayId | String? | → Day (opcional) |
| concept | String | |
| amount | Double | |
| currency | String | |
| category | Category | enum |
| receiptUri | String? | |

## Photo
| Campo | Tipo | |
|---|---|---|
| id | String (UUID) | 🔑 |
| noteId | String? | → JournalNote (opcional) |
| walletId | String? | → WalletEntry (opcional) |
| uri | String | |
| latitude | Double? | |
| longitude | Double? | |
| takenAt | Long | |

---

## Enumerados

**EntryType** → `FLIGHT · HOTEL · ACTIVITY · TRANSPORT · GENERIC`

**Status** (restaurantes) → `WITHOUT_RESERVATION · RESERVED · VISITED`

**Category** (gastos) → `FOOD · TRANSPORT · ACCOMMODATION · ACTIVITY · OTHER`

---

## Relaciones principales
- Trip → tiene muchos Day, WalletEntry, Restaurant, Expense
- Day → tiene muchos JournalNote, Restaurant, Expense
- JournalNote → tiene muchas Photo
- WalletEntry → tiene muchas Photo
