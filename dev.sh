#!/usr/bin/env bash
# Build the debug APK in WSL and install+launch it on every connected device
# (emulator and/or USB phone), which are owned by the Windows adb server and
# reached over shared localhost (WSL mirrored networking).
set -euo pipefail

export ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export PATH="$PATH:$ANDROID_HOME/platform-tools"
export ANDROID_ADB_SERVER_ADDRESS="${ANDROID_ADB_SERVER_ADDRESS:-127.0.0.1}"

APP_ID="com.example.coffeelog"
cd "$(dirname "$0")"

echo ">> building debug APK..."
./gradlew assembleDebug
APK="app/build/outputs/apk/debug/app-debug.apk"

mapfile -t DEVICES < <(adb devices | awk 'NR>1 && $2=="device"{print $1}')
if [ "${#DEVICES[@]}" -eq 0 ]; then
  echo "!! no devices. Start the emulator on Windows, or plug in the phone (USB debugging on)."
  exit 1
fi

for d in "${DEVICES[@]}"; do
  echo ">> [$d] install + launch"
  adb -s "$d" install -r "$APK"
  adb -s "$d" shell monkey -p "$APP_ID" -c android.intent.category.LAUNCHER 1 >/dev/null 2>&1
done
echo ">> done: ${DEVICES[*]}"
