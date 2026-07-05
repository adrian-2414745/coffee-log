# Design System

The Coffee Log visual language, distilled from the design mockup
([`design/coffee-brewing-journal.dc.html`](../design/coffee-brewing-journal.dc.html))
and the implemented theme ([`Color.kt`](../app/src/main/java/com/example/coffeelog/ui/theme/Color.kt),
[`Type.kt`](../app/src/main/java/com/example/coffeelog/ui/theme/Type.kt)).

See also [`style.md`](style.md) for the conceptual aesthetic direction behind these tokens.

**Character:** Functionalist / Braun-quiet. Molded inset panels, hairline borders,
monospaced numerals, restrained accent use. Two palettes ship as the app's light and
dark themes from a single component set.

---

## Fonts

Two families only — geometric sans for text, monospace for numbers.

| Family | Weights used | Used for |
| --- | --- | --- |
| **Archivo** (variable) | 400, 700, 800 | Titles, names, section/tag/field labels, buttons, segmented options, nav glyphs (`‹` `›`), stepper `−`/`+` |
| **JetBrains Mono** | 400, 500 | All numeric values, header subtitle / metadata, helper (descriptor) subtitles, unit suffixes, dates, notes body, mono text-field input |

The family files declare more weights (Archivo 400–900, Mono 400/500/700); the list above
is what the UI actually uses — **stay within it** so the two ramps stay legible. Loaded from
Google Fonts in the mockup; bundled as variable fonts (`R.font.archivo`,
`R.font.jetbrains_mono`) in the app.

### Type scale (as used)

Tracking is the raw `letterSpacing` in `sp` (what the code sets), not em. "Emphasis" names
a row in [Emphasis levels](#emphasis-levels-text-on-surface); accent = theme `tertiary`.
Every role below maps to exactly one entry here — pick the matching role rather than
inventing a new size/weight, and if a genuinely new role is needed, add it here first.

| Role | Family / weight | Size | Tracking | Case | Emphasis |
| --- | --- | --- | --- | --- | --- |
| Dashboard title ("Coffee Log") | Archivo 800 | 22 | — | — | Primary |
| Screen-header title | Archivo 800 | 18 | — | — | Primary |
| Card / row name, `SettingCard` title | Archivo 700 | 15 / 14 | — | — | Primary |
| Text-field input (name) | Archivo 700 | 15 | — | — | Primary |
| Form field label (`DOSE`, `NAME`…) | Archivo 700 | 14 | — | UPPER | Emphasis (.85); `*` at .4 |
| Section label (settings `DATA`) | Archivo 700 | 9 | 1.0 | UPPER | Secondary (.45) |
| Metric cell label | Archivo 700 | 9 | 0.5 | UPPER | Secondary (.5) |
| Status tag (`DISP` / `LEVEL` / `FAV`) | Archivo 700 | 8 | 0.7 | UPPER | accent if active, else Disabled (.28) |
| RATIO-pill label | Archivo 700 | 7 | 0.9 | UPPER | Secondary (.35) |
| Segmented option (`LIGHT`/`MEDIUM`/`DARK`) | Archivo 700 | 11 | 0.6 | UPPER | Primary selected, Secondary (.5) idle |
| Primary button (`SAVE`, `ADD`, `ADD COFFEE`, `ADD BREW`) | Archivo 800 | 13 | 0.9 | UPPER | onPrimary |
| Cancel button | Archivo 800 | 12 | 0.6 | UPPER | Secondary (.6) |
| Header subtitle / metadata (`ETHIOPIA GUJI · UNSAVED`) | Mono 500 | 10 | 0.8 | UPPER | Secondary (.45) |
| **Helper / descriptor subtitle** (settings "Restore from a JSON backup"; dashboard roast `(medium)`) | Mono 400 | 10 | — | sentence | Tertiary (.4) |
| Metric cell value | Mono 500 | 15 | — | — | Primary; Tertiary (.4) if muted |
| RATIO-pill / stepper value | Mono 500 | 14 | — | — | Primary |
| Mono text-field input (notes body, numeric fields) | Mono 400 | 13 | — | sentence | Primary; placeholder Tertiary (.4) |
| Unit suffix (`g`, `s`, `ml`, `/5`) | Mono 500 (stepper 400) | 9 | — | — | Tertiary (.4–.5) |
| Date stamp | Mono 500 | 11 | — | — | Tertiary (.4) |
| Stepper `−` / `+` glyph | Archivo 400 | 17 | — | — | Secondary (.5) |
| Back `‹` | Archivo 400 | 26 | — | — | Primary |
| Chevron `›` | Archivo 400 | 18 | — | — | Tertiary (.4) |

**Helper / descriptor subtitle** is the one recently reconciled: any short muted
explanatory line *next to or under* a name — a settings row's description, the dashboard's
parenthetical roast — is **Mono 400 / 10 / Tertiary (.4)**. It is deliberately distinct from
the **header subtitle** (Mono 500 / 10 / tracked / UPPERCASE), which is screen-level
metadata. Don't render descriptor text in Archivo or at the name's own size.

---

## Color

The **primary amber is shared across both themes** (primary action buttons — SAVE and the
`ADD COFFEE` / `ADD BREW` bars). Each
theme also has a **tertiary accent** that differs — amber in dark, red in light — used
for "active/live" affordances.

### Dark — Graphite (palette A)

| Token | Hex | Role |
| --- | --- | --- |
| Background / Surface | `#201E1B` | app background |
| surfaceContainer | `#2A2723` | cards, rows, tiles |
| surfaceContainerHighest | `#211E1A` | recessed inset panels (metric grid, fields, steppers) |
| outlineVariant | `#393530` | card / tile borders |
| onSurface | `#EDE7DB` | primary text |
| tertiary (accent) | `#E9B10A` amber | active toggles, RATIO value, stars, active tags |

### Light — Functionalist (palette B)

| Token | Hex | Role |
| --- | --- | --- |
| Background / Surface | `#EFEDE7` | app background |
| surfaceContainer | `#FAF9F5` | cards, rows, tiles |
| surfaceContainerHighest | `#E9E5DC` | recessed inset panels |
| outlineVariant | `#E8E4DA` | card / tile borders |
| onSurface | `#2B2A27` | primary text |
| tertiary (accent) | `#AE3B36` red | active toggles, RATIO, stars, active tags |

### Shared

| Token | Hex | Role |
| --- | --- | --- |
| primary | `#E9B10A` amber | primary action buttons — SAVE, `ADD COFFEE` / `ADD BREW` (both themes) |
| onPrimary | `#201E1B` (dark) / `#2B2A27` (light) | text/glyph on amber |
| device frame border | `#3A3631` (dark) / `#D6D2C7` (light) | screen outline in mockup |

### Accent semantics

- **Amber (`primary`)** — the one always-on brand color: the primary action buttons (SAVE,
  and the `ADD COFFEE` / `ADD BREW` bars).
- **Tertiary accent** (amber-dark / red-light) — "on / live / rated" states only:
  switches, the auto-computed RATIO value, star ratings, active status tags, notes caret,
  settings row icons.
- Everything else is neutral (onSurface at varying opacity). Accent is used sparingly.

### Emphasis levels (text on surface)

Neutral text uses one ink (`onSurface`) at descending opacities to signal importance.
Each step is a named **emphasis level** so it can be referenced without quoting a raw
alpha. Naming follows the common primary → secondary → tertiary → disabled convention;
the last row is a divider tone, not text.

| Level | Alpha | Applied to | Material 3 role |
| --- | --- | --- | --- |
| **Primary** (high emphasis) | 1.0 | names, metric values | `onSurface` |
| **Emphasis** (strong label) | ~.85 | form field labels | `onSurface` @ 85% |
| **Secondary** (medium emphasis) | ~.45–.50 | metric labels, subtitles, muted / placeholder values, `−`/`+` stepper glyphs | `onSurfaceVariant` |
| **Tertiary** (low emphasis) | ~.40 | chevrons, dates | `onSurfaceVariant` @ ~80% |
| **Disabled** | ~.28–.32 | inactive status tags | `onSurface` @ 38% |
| **Divider** (non-text) | ~.10–.12 | dividers, hairlines | `outlineVariant` |

**Notes for future work:**
- These are currently applied as ad-hoc `onSurface.copy(alpha = …)` calls. Material 3
  favours discrete color roles (`onSurface` for high, `onSurfaceVariant` for medium/low)
  over stacked opacities, which read cleaner over tinted/elevated surfaces — worth
  migrating to if the ramp is ever centralised.
- When formalising into tokens, the four text steps collapse cleanly to
  **primary / secondary / tertiary / disabled**; `Emphasis` (.85) is a fine-grained
  variant of primary and can fold into it if a strict scale is preferred.

---

## Shape, elevation & components

- **Corner radii:** cards/rows/buttons **6px**; inset panels & fields **5px**; segmented
  control outer 5px, selected segment inner 3px; status tags 4px.
- **Borders:** 1px `outlineVariant` on raised cards/tiles; **1px dashed** on the read-only
  RATIO pill.
- **Elevation:** raised cards use a soft drop shadow (`0 1px 2px`, heavier in dark);
  inset panels use an *inner* shadow (`inset 0 1px 3px`) to read as recessed/molded.
- **Metric grid:** 4-column grid; 1px cell dividers at ~10% onSurface; label (tiny, muted,
  tracked) stacked over mono value. 4 cells = dashboard summary, 8 cells = full brew card.
- **Toggle:** 32×18 pill track, 14px thumb; accent-filled when on, neutral track when off.
- **Buttons:** fixed bottom bar, 46px tall. Two forms: (1) full-width amber **add bar**
  (`AddBar`, dashboard / history) and (2) the **save + cancel** pair (`SaveCancelBar`, edit
  screens) — wide amber SAVE (flex 3) beside a quarter-width outlined CANCEL (flex 1). Both
  share the 6px radius, amber fill and Archivo-800/13 label. The screens have no floating
  action button — the primary add action lives in the bottom bar.
- **Status tags:** filled accent-tint + accent border when active; hollow neutral border,
  dimmed text when inactive.

## Spacing (recurring)

- Screen content horizontal padding: **16px**; header padding 16–20px.
- Card padding: **14×16px**; metric cell padding **8×10px**.
- Vertical gap between list cards/rows: **13px**.
- Form rows: 10px vertical padding with a hairline divider between each.
- Bottom action bar: 12px top / 16px bottom padding + navigation-bar inset.

## Iconography

Feather / Lucide-style line icons — 2px stroke, round caps/joins: settings gear,
import/export arrows, edit pencil. Navigation affordances use the typographic `›` (forward)
and `‹` (back) rather than icons.
