# Coffee Log — Architecture

Technical design for the Coffee Brewing Journal described in [PRD.md](PRD.md).
Visual design: [design/coffee-brewing-journal.dc.html](design/coffee-brewing-journal.dc.html) (canvas pulled from the Claude Design project; open in a browser — it shows all five screens in two palette directions). See §4 "Visual design".

## 1. Overview & tech stack

| Concern | Choice |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose (Material 3), single-activity |
| Navigation | Compose Navigation (`androidx.navigation:navigation-compose`) |
| Architecture pattern | MVVM — ViewModel + `StateFlow` UI state, unidirectional data flow |
| Persistence | Room (SQLite) |
| App preferences | Preferences DataStore (dark-theme toggle) |
| Serialization (import/export) | `kotlinx.serialization` (JSON) |
| Async | Kotlin coroutines + Flow |
| DI | Manual (a small `AppContainer` on the `Application` class) — Hilt is overkill for one module and two repositories |
| Min / target SDK | minSdk 34 (Android 14), targetSdk = latest stable |
| Build | Gradle (Kotlin DSL), version catalog (`libs.versions.toml`) |

Single Gradle module (`:app`). The app is small enough that multi-module structure would add ceremony without benefit; separation happens at the package level instead.

## 2. Package structure

```
io.github.adrian2414745.coffeelog/
├── CoffeeLogApp.kt            # Application; owns AppContainer
├── MainActivity.kt            # Single activity, sets Compose content
├── data/
│   ├── db/
│   │   ├── CoffeeLogDatabase.kt
│   │   ├── CoffeeDao.kt
│   │   ├── BrewDao.kt
│   │   └── entities.kt        # CoffeeEntity, BrewEntity
│   ├── transfer/
│   │   ├── ExportModels.kt    # @Serializable DTOs mirroring the JSON schema
│   │   └── DataTransferManager.kt  # export/import logic + validation
│   ├── CoffeeRepository.kt    # single repository over both DAOs
│   └── ThemeRepository.kt     # dark-theme flag in Preferences DataStore
├── ui/
│   ├── navigation/NavGraph.kt
│   ├── dashboard/             # Screen 1
│   ├── history/               # Screen 2
│   ├── brewedit/              # Screen 3 (new + edit mode)
│   ├── addcoffee/             # Screen 4
│   ├── settings/              # Screen 5
│   ├── theme/                 # Color.kt, Type.kt (tokens from the design canvas, §4)
│   └── components/            # shared: MetricGrid, StatusTag, StepperField, RatioPill, RatingStars, SegmentedField, SaveCancelBar, ConfirmDeleteDialog, ...
└── util/RatioFormatter.kt
```

Each screen package contains a `*Screen.kt` composable and a `*ViewModel.kt`.

## 3. Data model

### Room entities

```kotlin
@Entity(tableName = "coffees")
data class CoffeeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val roastLevel: String?,       // "LIGHT" | "MEDIUM" | "DARK"; from the design, not the PRD
    val createdAt: Long,           // epoch millis
)

@Entity(
    tableName = "brews",
    foreignKeys = [ForeignKey(
        entity = CoffeeEntity::class,
        parentColumns = ["id"], childColumns = ["coffeeId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("coffeeId")],
)
data class BrewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val coffeeId: Long,
    val groundsWeightG: Double,    // mandatory
    val liquidWeightG: Double,     // mandatory
    val liquidVolumeMl: Double?,   // optional
    val grindSize: Double?,        // e.g. 5.1
    val brewTimeSec: Int?,
    val waterTemp: Double?,        // unit-less by design (PRD)
    val usedDispenser: Boolean,
    val usedLeveler: Boolean,
    val rating: Int?,              // 1..5, null = unrated
    val isFavorite: Boolean,
    val favoritedAt: Long?,        // set when favorite toggled on; drives "most recently favorited"
    val notes: String?,            // free-text tasting notes; from the design, not the PRD
    val createdAt: Long,           // epoch millis; history sort key (newest first) and the date shown on brew cards
)
```

Notes:
- **Cascade delete**: deleting a coffee deletes all its brews (confirmed decision). The single confirm dialog covers both.
- **`favoritedAt`** exists because the dashboard shows the *most recently favorited* recipe per coffee, which is distinct from the newest brew. Toggling favorite off clears it; toggling on stamps the current time.
- **Golden ratio is computed, never stored**: `liquidWeightG / groundsWeightG`, displayed as `1:n` with n rounded to one decimal (e.g. `1:2.3`). Volume is never used in the ratio. Lives in `RatioFormatter`.
- Water temperature is a bare number with no unit, per the PRD.

### Key queries (DAOs)

- `CoffeeDao.observeDashboard()` — coffees sorted by `name COLLATE NOCASE`, each joined with its latest-favorited brew (subquery on `MAX(favoritedAt)`), returned as a Room `@Relation`-free projection (`DashboardRow`) via a JOIN. Returns `Flow<List<DashboardRow>>`.
- `BrewDao.observeForCoffee(coffeeId)` — brews ordered by `createdAt DESC`. Returns `Flow<List<BrewEntity>>`.
- Plain `insert` / `update` / `delete` suspend functions; `deleteCoffee` relies on cascade.

All reads are `Flow`s so screens update live after inserts, edits, deletes, and import.

## 4. UI layer

### Visual design

Source of truth: [design/coffee-brewing-journal.dc.html](design/coffee-brewing-journal.dc.html) — a design canvas exported from Claude Design showing all five screens in two palette directions with identical layout and seed data:

- **A — Graphite**: dark warm surfaces, amber accent.
- **B — Functionalist**: light "Braun-quiet" molded panels, red accent (amber kept for the Add/SAVE bars).

**Both directions ship**: B is the light theme, A is the dark theme — they share every layout, so the difference is confined to the color layer. The active theme is chosen by a **dark-theme toggle on the Settings screen** (see below), not by system dark mode alone.

#### Color tokens (`ui/theme/Color.kt` → Material 3 scheme)

| Token | B — light | A — dark (Graphite) | M3 slot |
|---|---|---|---|
| Screen background | `#EFEDE7` | `#201E1B` | `background` |
| Card surface | `#FAF9F5` | `#2A2723` | `surfaceContainer` |
| Card border | `#E8E4DA` | `#393530` | `outlineVariant` |
| Inset panel (metric grids, inputs) | `#E9E5DC` | `#211E1A` | `surfaceContainerHighest` |
| Primary text | `#2B2A27` | `#EDE7DB` | `onSurface` |
| Accent — Add bar, SAVE button | `#E9B10A` (amber) | `#E9B10A` | `primary` |
| Accent — ratio value, active tags/toggles, stars, icons | `#AE3B36` (red) | `#E9B10A` (amber) | `tertiary` |

Muted/secondary text is the primary text color at reduced alpha (design uses ~.4–.5); inactive tags/toggles use outline-only styling at low alpha.

#### Typography

- **Archivo** — titles, labels, buttons (weights 400–900; screen titles 800, field labels 700, tiny uppercase metric labels 700 with wide letter-spacing).
- **JetBrains Mono** — all numeric values (dose, grind, time, temp, yield, ratio, dates).
- Both bundled in `res/font/` (Google Fonts, OFL) and mapped in `ui/theme/Type.kt`; do not use the downloadable-fonts provider — the app is offline-first.

#### Recurring components (design → `ui/components/`)

| Design element | Composable |
|---|---|
| 4-column inset metric grid (DOSE/GRIND/TIME/TEMP, YIELD/VOL/RATIO/SCORE) | `MetricGrid` — used by dashboard favorite summary (1 row) and brew cards (2 rows) |
| Status tag chips `DISP` / `LEVEL` / `★ FAV` (read-only on history cards) | `StatusTag(active: Boolean)` |
| Stepper input `− value +` (dose, grind, time, yield, temp, vol) | `StepperField` |
| Read-only auto-ratio pill (dashed border, `AUTO` label) | `RatioPill` |
| Pill toggle switch (DISP/LEVEL/FAV on the brew form) | `M3 Switch`, accent-tinted |
| Star row (display on cards, input on form) | `RatingStars(editable: Boolean)` |
| Segmented control (LIGHT/MEDIUM/DARK roast) | `SegmentedField` (M3 `SingleChoiceSegmentedButtonRow`) |
| Bottom action bar — SAVE (3× width, filled amber) beside CANCEL (1×, outlined) | `SaveCancelBar` |
| Full-width amber bottom bar, e.g. "ADD COFFEE" / "ADD BREW" | `AddBar` |

Screen-level notes from the canvas: dashboard title is **"Coffee Log"**; history/brew/add/settings screens use a `‹` back affordance + screen title header; New Brew shows a `COFFEE NAME · UNSAVED` subtitle; brew cards show their date (from `createdAt`, e.g. `27 JUN 2026`) bottom-right; Settings rows are cards with icon + title + mono subtitle ("Restore from a JSON backup" / "Download all brews as JSON").

#### Design deltas vs the PRD

The canvas adds things the PRD never specified — treat the design as authoritative; schema and export format below already include them:

1. **NOTES** — free-text field on the brew form (`BrewEntity.notes`).
2. **ROAST LEVEL** — LIGHT/MEDIUM/DARK segmented control on Add Coffee (`CoffeeEntity.roastLevel`).
3. **Brew date displayed** on history cards (already stored as `createdAt`).
4. **Steppers instead of plain text fields** for numeric input (tap −/+ to nudge; the center value stays directly editable so arbitrary values remain possible).
5. **Auto-calculated RATIO shown live on the brew form**, read-only.
6. Button labels: **"SAVE"** and **"ADD"** instead of the PRD's generic "SAVE"; the PRD's "+" FAB is implemented as a full-width labelled bottom bar (`AddBar`: "ADD COFFEE" / "ADD BREW") instead of a circular FAB.
7. Tools/favorite on history cards are **read-only tags**, editable only on the form.

Not in the design (PRD still governs): empty states, long-press delete + confirm dialog, edit mode entry by tapping a card.

#### Theme selection

- Settings gains a **"Dark theme" switch** (a third row alongside Import/Export, same card style).
- Persisted in **Preferences DataStore** (`ThemeRepository` in `data/`, exposed as `Flow<Boolean?>`): `null` = not yet set → follow system dark mode; `true`/`false` = explicit user choice. First toggle flips it to explicit.
- `MainActivity` collects the flow and passes `darkTheme` into `CoffeeLogTheme`, so the whole app switches live.
- The theme preference is **device-local** — deliberately excluded from JSON export/import.

### Navigation graph

Type-safe routes (Navigation Compose 2.8+ serializable routes):

```
Dashboard                    (start)
 ├── History(coffeeId)
 │    └── BrewEdit(coffeeId, brewId?)   # brewId == null → new, else edit
 ├── AddCoffee
 └── Settings
```

- System back from History/AddCoffee/Settings → Dashboard; from BrewEdit → History. This matches the PRD because back-stack order gives it for free — no custom back handling except BrewEdit, where back = CANCEL (discard), which is also the default since nothing is saved until SAVE.
- SAVE on BrewEdit/AddCoffee calls the ViewModel then `popBackStack()`.

### Screens ↔ ViewModels

| Screen | ViewModel state | Events |
|---|---|---|
| Dashboard | `List<DashboardRow>` (coffee + optional favorite summary) | delete coffee (after confirm dialog) |
| History | coffee name + `List<BrewEntity>` | delete brew (after confirm) |
| BrewEdit | form field state, validation errors, mode (new/edit) | save, field edits |
| AddCoffee | name field, validation error | save |
| Settings | import/export result (snackbar message), dark-theme flag | import(uri), export(uri), toggle dark theme |

Conventions:
- ViewModels expose one `StateFlow<UiState>` and take events as function calls; composables are stateless renderers.
- Confirm-delete dialogs are UI-local state (a `rememberSaveable` holding the pending target), not ViewModel state.
- Form fields in BrewEdit are held as strings and parsed on save, so partial input like `5.` never fights the keyboard. Numeric keyboards (`KeyboardType.Decimal`/`Number`) per field.
- Validation: SAVE is enabled only when mandatory fields parse (grounds weight, liquid weight on BrewEdit; non-blank name on AddCoffee). Rating and favorite default to unset/off.
- The `AddBar` ("ADD COFFEE" / "ADD BREW") and the `SaveCancelBar` (SAVE/CANCEL) are fixed via `Scaffold` bottom-bar slots.
- Empty states (Dashboard, History) render nothing but the `AddBar`, per the PRD.

## 5. Import / export

Handled by `DataTransferManager`, invoked from `SettingsViewModel`. No runtime permissions needed — both use the Storage Access Framework:

- **Export**: `ActivityResultContracts.CreateDocument("application/json")` → serialize full DB to the chosen URI.
- **Import**: `ActivityResultContracts.OpenDocument` → read, parse, validate, then replace.

### JSON schema (versioned)

```json
{
  "schemaVersion": 1,
  "exportedAt": "2026-07-04T10:00:00Z",
  "coffees": [
    {
      "name": "Ethiopia Yirgacheffe",
      "roastLevel": "MEDIUM",
      "createdAt": 1719999999000,
      "brews": [
        {
          "groundsWeightG": 18.0,
          "liquidWeightG": 36.5,
          "liquidVolumeMl": null,
          "grindSize": 5.1,
          "brewTimeSec": 28,
          "waterTemp": 93,
          "usedDispenser": true,
          "usedLeveler": false,
          "rating": 4,
          "isFavorite": true,
          "favoritedAt": 1719999999000,
          "notes": "Juicy, bergamot top, tea-like finish.",
          "createdAt": 1719999999000
        }
      ]
    }
  ]
}
```

Brews are nested under coffees so the file has no database IDs — import regenerates them, which keeps the format stable across devices.

### Import semantics (confirmed decisions)

1. Read and parse the entire file; validate schema version and every record (mandatory fields present, rating in 1..5 if set).
2. Only if fully valid: in a **single Room transaction**, delete all rows and insert the imported data (replace-all, per PRD).
3. On any failure the transaction never starts — existing data is untouched and the user sees an error snackbar with the reason.

## 6. Error handling & edge cases

- Division for the ratio is safe: grounds weight is mandatory and validated `> 0` at input time.
- Duplicate coffee names are allowed (PRD doesn't forbid them; rows are keyed by ID).
- DB writes run on `Dispatchers.IO` via suspend DAOs; UI never blocks.
- Room `exportSchema = true` with schemas checked into `app/schemas/` from day one, so future migrations are testable.

## 7. Testing strategy

- **Unit**: `RatioFormatter`, BrewEdit form parsing/validation, `DataTransferManager` round-trip (export → import yields identical data) and rejection of malformed files.
- **DAO tests**: dashboard query (alphabetical sort, most-recently-favorited join), cascade delete — Room in-memory DB, run as instrumented tests on the emulator.
- **UI (light)**: Compose UI tests for the two empty states and the delete-confirm flow.

## 8. Dev workflow

Per [environment-setup.md](environment-setup.md): build in WSL2, run on the Windows emulator or Wi-Fi ADB phone via `./dev.sh`. Nothing in this architecture depends on the hybrid setup.
