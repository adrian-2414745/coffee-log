# Coffee Log

Android app (`com.example.coffeelog`), built in WSL2, run on a Windows emulator and a real phone.

## Docs
- [docs/PRD.md](docs/PRD.md) — product requirements.
- [docs/navigation.md](docs/navigation.md) — screen navigation graph (Mermaid).
- [docs/environment-setup.md](docs/environment-setup.md) — hybrid WSL2 + Windows build/run setup.
- [docs/connection.md](docs/connection.md) — connecting the phone over Wi-Fi ADB.
- [docs/architecture.md](docs/architecture.md) — technical design.
- [docs/tasks.md](docs/tasks.md) — implementation plan.
- [docs/style.md](docs/style.md) — visual style reference (aesthetic direction).
- [docs/design-style.md](docs/design-style.md) — design system: fonts, color tokens & component specs.

## Dev loop
- `./dev.sh` — build APK + install + launch on all connected devices.
- `./adb-wifi.sh <port>` — reconnect the phone over Wi-Fi (see docs/connection.md).

# General rules
- never assume anything, always check or ask the user
- when adding/updating features always check/adapt/add unit tests
