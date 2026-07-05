# Coffee Log — Development Environment Setup

A hybrid **WSL2 + Windows 11** setup: build the Android app in WSL2, run it on the
Windows emulator and a real USB phone. Verified end-to-end — the hello-world app
builds in WSL2 and runs on the Windows emulator.

## Architecture

```
WSL2 (Ubuntu 24.04)                    Windows 11
─────────────────────                  ─────────────────────
build side                             runtime side
  Android SDK ~/Android/Sdk              Android SDK D:\ws\android-sdk
  JDK 21 (sdkman)                        JDK 22 (C:\Program Files\OpenJDK)
  Gradle 8.11.1                          Emulator + Android 15 system image
  project ~/ws/coffee-log                AVD "coffeelog" (Pixel 6)
  adb client ──────────────────────────► adb server (owns emulator + USB phone)
                 shared localhost:5037
                 (WSL mirrored networking)
```

Why hybrid: the emulator gets native Windows hardware acceleration, while the
build/agent workflow stays in Linux. Mirrored networking makes `localhost` shared
between WSL and Windows, so the adb bridge needs no port juggling and **no usbipd**.

## What's installed

### WSL2 — the build side (`~/ws/coffee-log`)
- Android SDK at `~/Android/Sdk`: cmdline-tools, platform-tools r37, build-tools 35, platform android-35
- JDK 21 (via sdkman), Gradle 8.11.1 (via sdkman)
- Minimal Kotlin app (`io.github.adrian2414745.coffeelog`) — builds an ~813 KB debug APK in ~20s
- Env + adb-bridge vars persisted in `~/.bashrc` under marker `# --- ANDROID SDK (Claude setup) ---`:
  ```bash
  export ANDROID_HOME="$HOME/Android/Sdk"
  export ANDROID_SDK_ROOT="$ANDROID_HOME"
  export PATH="$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin"
  export ANDROID_ADB_SERVER_ADDRESS=127.0.0.1
  ```

### Windows 11 — the runtime side (`D:\ws\android-sdk`)
- Android SDK: emulator + `system-images;android-35;google_apis;x86_64` + platform-tools
- JDK 22 at `C:\Program Files\OpenJDK\jdk-22.0.2` (was already present)
- AVD `coffeelog` (Pixel 6), hardware-accelerated
- Single adb server runs here and owns the emulator (and the USB phone)

### The bridge
WSL adb client → Windows adb server over shared `localhost:5037`, enabled by
`ANDROID_ADB_SERVER_ADDRESS=127.0.0.1` plus mirrored networking. adb versions must
match on both sides (both r37). WSL's `adb devices` lists `emulator-5554` directly.

## Everyday dev loop

```bash
./dev.sh          # builds the APK in WSL, installs + launches on all connected devices
```

Or manually:
```bash
./gradlew assembleDebug
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s emulator-5554 shell monkey -p io.github.adrian2414745.coffeelog -c android.intent.category.LAUNCHER 1
```

Relaunch the emulator later (from Windows):
```
D:\ws\android-sdk\emulator\emulator.exe -avd coffeelog
```

## Connecting a real phone

No usbipd needed — the phone plugs into Windows and appears in WSL via the bridge:

1. On the phone: Settings → About → tap **Build number** 7× to unlock Developer Options,
   then enable **USB debugging**.
2. Plug into the PC via USB; tap **Allow** on the RSA authorization prompt.
3. From WSL: `adb devices` — the phone appears alongside the emulator.
4. `./dev.sh` installs to both at once.

Troubleshooting: if the phone doesn't appear, run `adb kill-server` on the Windows
side so it re-owns the USB device, then `adb devices` again in WSL.

## Notes
- Open a fresh WSL shell (or `source ~/.bashrc`) so the SDK/adb env vars are loaded.
- Keep the project on the WSL filesystem (`~/ws/coffee-log`), not `/mnt/c` — Gradle is
  dramatically slower over the 9P bridge.
