# Coffee Log

Android app (`com.example.coffeelog`), built in WSL2, run on a Windows emulator and a real phone.

## Docs
- [PRD.md](PRD.md) — product requirements.
- [environment-setup.md](environment-setup.md) — hybrid WSL2 + Windows build/run setup.
- [connection.md](connection.md) — connecting the phone over Wi-Fi ADB.

## Dev loop
- `./dev.sh` — build APK + install + launch on all connected devices.
- `./adb-wifi.sh <port>` — reconnect the phone over Wi-Fi (see connection.md).
