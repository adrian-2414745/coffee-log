#!/usr/bin/env bash
# Build the debug APK in WSL and install+launch it on the emulator only
# (skips any connected USB/Wi-Fi phone), reached over shared localhost
# (WSL mirrored networking) via the Windows adb server.
set -euo pipefail

APP_ID="io.github.adrian2414745.coffeelog"
cd "$(dirname "$0")"
source ./adb-env.sh
ensure_adb_server

echo ">> building debug APK..."
./gradlew assembleDebug
APK="app/build/outputs/apk/debug/app-debug.apk"

mapfile -t DEVICES < <(adb devices | awk 'NR>1 && $2=="device" && $1 ~ /^emulator-/{print $1}')
if [ "${#DEVICES[@]}" -eq 0 ]; then
  echo "!! no emulator running. Start it on Windows: D:\\ws\\android-sdk\\emulator\\emulator.exe -avd coffeelog"
  exit 1
fi

for d in "${DEVICES[@]}"; do
  echo ">> [$d] install + launch"
  adb -s "$d" install -r "$APK"
  adb -s "$d" shell monkey -p "$APP_ID" -c android.intent.category.LAUNCHER 1 >/dev/null 2>&1
done
echo ">> done: ${DEVICES[*]}"
