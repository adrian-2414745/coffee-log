# Releasing Coffee Log to F-Droid

A checklist of everything we need to do before Coffee Log can be submitted to
F-Droid, based on the [Submitting to F-Droid Quick Start Guide](https://f-droid.org/docs/Submitting_to_F-Droid_Quick_Start_Guide/).

## How F-Droid works (context)

F-Droid does **not** take a prebuilt APK. It builds the app itself from our
public source, at a specific git commit, in an isolated environment, and signs
it with its own key. So the whole job is: make the repo build cleanly from
source with no proprietary bits, add the required metadata, then open a merge
request against the [`fdroiddata`](https://gitlab.com/fdroid/fdroiddata) repo
containing a build recipe.

## Current status

Good news — the app is already largely compliant:

- ✅ **All dependencies are FOSS.** Only AndroidX, Compose, Kotlin, Room and
  DataStore. No Firebase, no Google Play Services, no proprietary SDKs.
- ✅ **No tracking / ads / anti-features.** The manifest requests **zero
  permissions** (no `INTERNET`), so there is nothing to declare as an
  anti-feature.
- ✅ **Release build is unsigned** (no `signingConfig` in `app/build.gradle.kts`)
  and `isMinifyEnabled = false` — F-Droid supplies its own signature, so this is
  fine.
- ✅ Custom launcher icon already exists.
- ✅ Standard Gradle project layout — easy for F-Droid's build system.

The remaining work is the checklist below.

> **Progress (updated 2026-07-05):** Blockers 1 and 2 are **done**, and the text
> half of blocker 5 is **done**. Remaining: repo visibility (3), release tag (4),
> metadata images (5), and the submission steps (6–8). See the per-section notes.

---

## Blockers (must fix before submission)

### 1. Change the application ID — ✅ DONE

The app ID is now **`io.github.adrian2414745.coffeelog`** (the F-Droid
convention for GitHub-hosted apps; the `-` in the username was dropped because
Java/Kotlin package segments can't contain hyphens).

Completed:
- ✅ `namespace` and `applicationId` updated in `app/build.gradle.kts`.
- ✅ Kotlin source moved from `com/example/coffeelog` to
  `io/github/adrian2414745/coffeelog` (main, test, androidTest) via `git mv`,
  with all `package`/`import` lines rewritten across the 49 source files.
- ✅ `AndroidManifest.xml` needs no change (it uses relative `.MainActivity` /
  `.CoffeeLogApp` names that resolve against `namespace`).
- ✅ `dev.sh` `APP_ID` and doc references (`CLAUDE.md`, `architecture.md`,
  `tasks.md`, `environment-setup.md`) updated.
- ✅ Verified: `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest` passes.

The `fdroiddata` metadata file will be named
`io.github.adrian2414745.coffeelog.yml`.

### 2. Add a FOSS license — ✅ DONE

- ✅ **GPLv3** chosen. `LICENSE` file added at the repo root (verbatim
  `gpl-3.0.txt` from gnu.org).
- SPDX identifier for the metadata `License:` field: **`GPL-3.0-only`**.
- TODO (optional): add per-file license headers and/or a `README` note.

### 3. Make the source repository public

The build recipe points at our public git repo
(`https://github.com/adrian-2414745/coffee-log`). Confirm it is **public** and
will stay that way. Ensure no secrets are committed (`local.properties`,
keystores, and `*.apk`/`*.aab` are already git-ignored — good).

### 4. Tag the release commit

F-Droid builds a specific git tag. Each release needs a tag matching the
version.

- Bump `versionCode`/`versionName` as needed (currently `1` / `"1.0"`).
- Create an annotated tag on the release commit, e.g. `git tag -a v1.0 -m "1.0"`.
- Push the tag. The build recipe references either this tag or its commit hash.

### 5. Add fastlane metadata to the repo — ⚠️ PARTIALLY DONE (text done, images left)

F-Droid pulls store text and screenshots from a `fastlane/` tree **in our
repo**. Current state:

```
fastlane/metadata/android/en-US/
├── title.txt                 ✅ "Coffee Log"
├── short_description.txt      ✅ 69 chars, no trailing period
├── full_description.txt       ✅ written (adapted from PRD.md)
├── changelogs/
│   └── 1.txt                  ✅ versionCode 1 changelog
└── images/
    └── phoneScreenshots/
        └── .gitkeep           ⬅ placeholder only
```

Still TODO (binary assets — must be produced from the running app):
- ⬜ `images/phoneScreenshots/1.png`, `2.png`, … — real screenshots from the
  running app (dashboard, coffee history, new-brew input, settings). None exist
  yet; the `design/` folder only has HTML mockups. Capture with
  `adb exec-out screencap -p > 1.png` on the emulator/phone. Screenshots must be
  raster (PNG/JPG). Once real ones exist, remove the `.gitkeep` placeholder.

**No separate icon.png needed.** F-Droid derives the app's listing icon directly
from the launcher icon in the built APK, so our adaptive **vector** launcher icon
is enough. The optional `images/icon.png` slot only accepts a raster PNG (not
SVG/vector); skip it and let F-Droid extract the icon from the APK.

---

## Submission steps (once the blockers are done)

### 6. Verify a clean release build from a fresh checkout

F-Droid builds in a clean container with no access to Android Studio or our
local SDK. Sanity-check:

```bash
./gradlew clean :app:assembleRelease
```

Confirm it succeeds with only the committed sources (no reliance on
`local.properties` beyond the SDK path, which F-Droid provides).

### 7. Write and test the `fdroiddata` build recipe

- Fork [`fdroiddata`](https://gitlab.com/fdroid/fdroiddata), branch named after
  the app ID.
- Create `metadata/<applicationId>.yml` with:
  - Descriptive fields: `License`, `AuthorName`, `WebSite`, `SourceCode`,
    `IssueTracker`, `Categories` (e.g. `- Sports & Health` or
    `- Food`... pick from F-Droid's category list), `Changelog`.
  - `RepoType: git` and `Repo:` pointing at our GitHub URL.
  - A `Builds:` block: `versionName`, `versionCode`, `commit` (the tag),
    `subdir: app`, `gradle: [yes]`.
  - `AutoUpdateMode` / `UpdateCheckMode` (Gradle version is in the standard
    place, so `Tags` or `HTTP` auto-update should work with minimal config).
  - `CurrentVersion` / `CurrentVersionCode`.
- Test locally with `fdroidserver` (Docker) before submitting:

  ```bash
  fdroid readmeta
  fdroid lint <applicationId>
  fdroid rewritemeta <applicationId>
  fdroid checkupdates <applicationId>
  fdroid build <applicationId>
  ```

  (Needs ~5 GB storage + ~2 GB download for the build container.)

### 8. Open the merge request

- Commit as `New App: <applicationId>`, push, open an MR against `fdroiddata`
  `master`.
- Respond to reviewer feedback in the thread.
- After merge, expect ~24–48h before it appears in the repo, plus extra time for
  the website pages.

---

## Nice-to-have (not required for acceptance)

- **`README.md`** at the repo root — good project hygiene and a landing page for
  people who find the source.
- **Reproducible builds** — lets F-Droid verify our own signed APK matches
  theirs. Kotlin/Java apps are the easy case, but it's optional and can be added
  later.
- **`AutoUpdateMode: Version`** with proper tagging so future releases are
  picked up automatically without a new MR — just tag and bump.

---

## Quick checklist

- [x] Change `applicationId`/`namespace` off `com.example.*` + move source package → `io.github.adrian2414745.coffeelog`
- [x] Add `LICENSE` file (record SPDX id) → GPLv3, `GPL-3.0-only`
- [ ] Confirm GitHub repo is public, no secrets committed
- [ ] Tag the release commit (`v1.0`)
- [x] Add `fastlane/metadata/android/en-US/` (title, descriptions, changelog)
- [ ] Capture and add phone screenshots (icon.png not needed — F-Droid uses the APK's vector launcher icon)
- [ ] Verify `./gradlew clean :app:assembleRelease` from a clean checkout
- [ ] Fork `fdroiddata`, write `metadata/<applicationId>.yml`
- [ ] Test with `fdroid lint` / `fdroid build` (Docker)
- [ ] Open merge request, address review feedback
