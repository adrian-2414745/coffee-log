# Coffee Log — Stories & Tasks

Implementation plan derived from [PRD.md](PRD.md), [architecture.md](architecture.md), and the design canvas [design/coffee-brewing-journal.dc.html](design/coffee-brewing-journal.dc.html) (both palettes ship: B = light theme, A = Graphite dark theme; Settings has a dark-theme toggle).
Stories are ordered so the app is buildable, runnable (`./dev.sh`), and manually QA-able after **every** story.

Legend: `[ ]` open · `[x]` done

---

## Story 1 — Project scaffold & app shell

*Goal: an installable app that launches to an empty dashboard shell.*

- [x] 1.1 Create the Android project: Kotlin, Compose (Material 3), single module `:app`, package `com.example.coffeelog`, minSdk 34, Kotlin DSL + version catalog.
- [x] 1.2 Add dependencies: Compose BOM, navigation-compose, Room (+ ksp), kotlinx-serialization-json, lifecycle-viewmodel-compose, datastore-preferences.
- [x] 1.3 `ui/theme/`: both palettes from the design canvas (B light, A Graphite dark) as Material 3 color schemes per architecture.md §4; bundle Archivo + JetBrains Mono in `res/font/` and map them in `Type.kt`. Theme follows system dark mode for now (toggle arrives in Story 8).
- [x] 1.4 `CoffeeLogApp` (Application) with empty `AppContainer`; `MainActivity` setting Compose content wrapped in `CoffeeLogTheme`.
- [x] 1.5 `NavGraph` with a placeholder Dashboard route as the start destination (bare `Scaffold`, "Coffee Index" title, gear icon and amber "+" FAB doing nothing yet).
- [x] 1.6 Verify `./dev.sh` builds, installs, and launches on the emulator and phone.

**Manual QA**
- App installs and opens without crashing on both devices.
- Empty dashboard shows "Coffee Index" title, gear icon top-right, amber "+" FAB bottom-right.
- Colors/fonts match the design canvas; flipping system dark mode switches between the light (B) and Graphite (A) palettes.

---

## Story 2 — Data layer (Room)

*Goal: database in place, verified by tests (no visible UI change).*

- [x] 2.1 `CoffeeEntity` (with `roastLevel`), `BrewEntity` (with `favoritedAt`, `notes`, cascade FK, index) per architecture.md §3.
- [x] 2.2 `CoffeeDao` (insert, delete, `observeDashboard()` with alphabetical `COLLATE NOCASE` sort + latest-favorited-brew join) and `BrewDao` (insert, update, delete, `observeForCoffee()` newest-first).
- [x] 2.3 `CoffeeLogDatabase` with `exportSchema = true`, schemas dir checked in; wire DB + `CoffeeRepository` into `AppContainer`.
- [x] 2.4 Instrumented DAO tests: alphabetical sort, newest-first brews, latest-favorited join, cascade delete.

**Manual QA**
- App still launches (DB created lazily).
- `./gradlew connectedDebugAndroidTest` passes on the emulator.

---

## Story 3 — Add coffee (Screen 4) & dashboard list (Screen 1, basic)

*Goal: first real user flow — create coffees and see them listed.*

- [x] 3.1 `AddCoffeeScreen` + ViewModel: mandatory name field, ROAST LEVEL segmented control (LIGHT/MEDIUM/DARK, via shared `SegmentedField`), fixed bottom `SaveCancelBar` ("ADD TO INDEX" 3× + "CANCEL" 1×, per the design); save disabled while name is blank; save persists and pops back; CANCEL/back discards.
- [x] 3.2 `DashboardViewModel` exposing `Flow` of dashboard rows; `DashboardScreen` renders the coffee list (names only for now), empty state = FAB only.
- [x] 3.3 Wire navigation: FAB → AddCoffee; back/CANCEL → Dashboard.

**Manual QA**
- Fresh install shows only the "+" FAB (empty state).
- Add "Ethiopia" then "Brazil" → list shows Brazil, Ethiopia (alphabetical).
- CANCEL and system back both discard typed input.
- "ADD TO INDEX" stays disabled until a name is entered; roast level selection renders like the design's segmented control.
- Kill and relaunch the app → coffees persist.

---

## Story 4 — Delete coffee (long-press)

*Goal: dashboard rows can be removed safely.*

- [x] 4.1 Shared `ConfirmDeleteDialog` component ("Are you sure you want to delete?" with [Yes]/[Cancel]).
- [x] 4.2 Long-press on a dashboard row opens the dialog; Yes deletes the coffee (cascade), Cancel dismisses.

**Manual QA**
- Long-press a coffee → dialog appears; Cancel keeps it, Yes removes it.
- Deleting the last coffee returns the dashboard to the empty state.
- Normal tap does not trigger the dialog.

---

## Story 5 — Log a brew (Screen 3, new mode) & history list (Screen 2)

*Goal: the core loop — record brews and review them per coffee.*

- [x] 5.1 `HistoryScreen` + ViewModel: coffee name in the top bar, brews newest-first, empty state = FAB only; dashboard row tap → History; back → Dashboard.
- [x] 5.2 `RatioFormatter` (`1:n`, one decimal, weight only) + unit tests.
- [x] 5.3 Shared components per the design: `MetricGrid` (4-column inset grid), `StatusTag` (DISP/LEVEL/★ FAV chips, active vs muted), `RatingStars`.
- [x] 5.4 Brew card composable per the design canvas: two-row `MetricGrid` (DOSE/GRIND/TIME/TEMP + YIELD/VOL/RATIO/SCORE, `—` for missing VOL, accent-colored ratio), read-only `StatusTag` row, date (`createdAt`, e.g. `27 JUN 2026`) bottom-right.
- [x] 5.5 `BrewEditScreen` + ViewModel (new mode) per the design: `StepperField` rows (−/+ nudge, center value directly editable, numeric keyboard) for DOSE/GRIND/TIME/YIELD/TEMP/VOL, live read-only `RatioPill` (AUTO), switches for DISP/LEVEL/FAV, star selector, NOTES free-text area, `COFFEE NAME · UNSAVED` subtitle, fixed `SaveCancelBar` ("SAVE BREW" + "CANCEL").
- [x] 5.6 Validation: save enabled only when dose (grounds) and yield (liquid weight) parse as numbers > 0; favorite ON stamps `favoritedAt`.
- [x] 5.7 Wire navigation: History FAB → BrewEdit(new); SAVE BREW persists and pops to History; CANCEL/back discards.
- [x] 5.8 Unit tests for form parsing/validation.

**Manual QA**
- Tap a coffee → empty history with only the FAB.
- Add a brew (dose 18 g, yield 36.5 g, grind 5.1, 28 s, temp 93, dispenser on, leveler off, 4 stars, favorite on, a short note).
- Card matches the design: metric grid with all values, active DISP tag + muted LEVEL tag + active ★ FAV, ratio `1:2.0` in accent color, today's date bottom-right.
- RATIO pill on the form updates live as dose/yield change.
- Steppers nudge values; typing directly into the value still works.
- Add a second brew → it appears at the top (newest first).
- SAVE BREW disabled until dose and yield are valid; optional fields can stay empty (VOL shows `—` on the card).
- CANCEL and system back discard the form; back from History returns to Dashboard.

---

## Story 6 — Edit & delete brews (Screen 3, edit mode)

*Goal: full CRUD on brews.*

- [x] 6.1 BrewEdit edit mode: route takes optional `brewId`; ViewModel pre-fills all fields from the existing brew; button still labeled "SAVE"; SAVE updates in place (preserve `createdAt`; stamp/clear `favoritedAt` only when the favorite switch actually changes).
- [x] 6.2 Tap a history card → BrewEdit in edit mode.
- [x] 6.3 Long-press a history card → shared confirm dialog → delete brew.

**Manual QA**
- Tap a brew → form pre-filled exactly as saved (including switches, stars, favorite).
- Change grind and rating, SAVE → card updates, position in list unchanged.
- Edit + CANCEL → no changes applied.
- Long-press a brew → Yes deletes it; deleting the last brew shows the history empty state.

---

## Story 7 — Dashboard favorite summary

*Goal: Screen 1 fully matches the PRD.*

- [x] 7.1 Extend the dashboard row UI: when a coffee has favorite brews, show the most recently favorited one's main metrics as a single-row `MetricGrid` (DOSE/GRIND/TIME/TEMP), per the design canvas.
- [x] 7.2 Rows without favorites show the name only.

**Manual QA**
- Coffee with no favorites → plain name row.
- Favorite one brew → its metrics appear on the dashboard.
- Favorite a second (older-created) brew afterwards → dashboard switches to it (most recently *favorited* wins).
- Unfavorite it → dashboard falls back to the previous favorite; unfavorite all → plain row.

---

## Story 8 — Settings: export, import & dark-theme toggle (Screen 5)

*Goal: full data portability + explicit theme control.*

- [x] 8.1 `SettingsScreen` + ViewModel: Import Data / Export Data rows styled per the design canvas (card rows with icon, title, mono subtitle); gear icon → Settings; back → Dashboard.
- [x] 8.2 `ThemeRepository` (Preferences DataStore): `Flow<Boolean?>` — `null` = follow system, else explicit; "Dark theme" switch row on Settings; `MainActivity` collects it and drives `CoffeeLogTheme` live.
- [x] 8.3 Export DTOs (`schemaVersion: 1`, brews nested under coffees incl. `roastLevel`/`notes`, no DB IDs; theme preference deliberately excluded) + `DataTransferManager.export()` via `CreateDocument("application/json")`.
- [x] 8.4 `DataTransferManager.import()` via `OpenDocument`: parse + validate everything first, then replace-all in a single Room transaction; on any error keep existing data and show the reason in a snackbar.
- [x] 8.5 Success/failure snackbars for both actions.
- [x] 8.6 Unit tests: export→import round-trip identity; rejection of malformed JSON, wrong schema version, invalid rating.

**Manual QA**
- Toggle "Dark theme" → whole app switches to the Graphite palette instantly; toggle survives app restart; before the first toggle the app follows system dark mode.
- Export → pick a location → JSON file exists, readable, includes roast level and notes.
- Add/delete some data, then import the file → state is exactly restored (dashboard favorites included).
- Import a bogus file (e.g. a photo or hand-broken JSON) → error snackbar, existing data untouched.
- Export on phone, import on emulator (or vice versa) → identical data across devices.

---

## Story 9 — Polish & release readiness

*Goal: production-quality pass; no new features.*

- [x] 9.1 App icon and app name.
- [x] 9.2 Visual pass against the design canvas: spacing, typography, both palettes side-by-side with `design/coffee-brewing-journal.dc.html`, star/favorite iconography consistency.
- [x] 9.3 Compose UI tests: both empty states, delete-confirm flow.
- [x] 9.4 Edge-case sweep: rotation preserves in-progress form input (`rememberSaveable`/ViewModel), very long coffee names ellipsize, large brew counts scroll smoothly.
- [x] 9.5 Full manual regression of stories 3–8 on both emulator and phone.

**Manual QA**
- Rotate mid-edit on Screen 3 → nothing typed is lost.
- Both themes look correct on all five screens and match the canvas.
- Icon and name correct in the launcher.
