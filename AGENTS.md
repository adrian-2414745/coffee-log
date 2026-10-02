# Coffee Log

Android app (`io.github.adrian2414745.coffeelog`), built in WSL2, run on a Windows emulator and a real phone.

## Docs
- [docs/PRD.md](docs/PRD.md) — product requirements.
- [docs/navigation.md](docs/navigation.md) — screen navigation graph (Mermaid).
- [docs/environment-setup.md](docs/environment-setup.md) — hybrid WSL2 + Windows build/run setup.
- [docs/connection.md](docs/connection.md) — connecting the phone over Wi-Fi ADB.
- [docs/architecture.md](docs/architecture.md) — technical design.
- [docs/style.md](docs/style.md) — visual style reference (aesthetic direction).
- [docs/design-style.md](docs/design-style.md) — design system: fonts, color tokens & component specs.
- docs/archive - folder to keep historical documents (plans, research) for finished features

## Dev loop
- `./dev.sh` — build APK + install + launch on all connected devices.
- `./dev-emulator.sh` — build APK + install + launch on the emulator only (skips the phone).
- `./adb-wifi.sh <port>` — reconnect the phone over Wi-Fi (see docs/connection.md).

## General rules
- never assume anything, always check or ask the user
- when adding/updating features always check/adapt/add unit tests

## F-Droid release
Read only when releasing:
[docs/release-to-fdroid.md](docs/release-to-fdroid.md), [docs/signing.md](docs/signing.md)
repo at /home/blitz/ws/f-droid-data:
see `CONTRIBUTING.md` and `.gitlab/merge_request_templates/App inclusion.md`

## Lessons learned
Read agents/lessons-learned.md 
Log a lesson whenever you hit and fix a non-obvious problem — the goal is to never repeat the same mistake. Format: problem -> solution. One line per lesson, no debugging story.
