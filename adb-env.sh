#!/usr/bin/env bash
# Shared adb setup, sourced by dev.sh / dev-emulator.sh / adb-wifi.sh.
# The adb server runs on Windows (owns emulator + phone); the WSL client reaches it
# over shared localhost (mirrored networking). The WSL client won't start that
# server itself — it just hangs — so start it via the Windows adb.exe if needed.

export ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export PATH="$PATH:$ANDROID_HOME/platform-tools"
export ANDROID_ADB_SERVER_ADDRESS="${ANDROID_ADB_SERVER_ADDRESS:-127.0.0.1}"

WIN_ADB="${WIN_ADB:-/mnt/d/ws/android-sdk/platform-tools/adb.exe}"

ensure_adb_server() {
  if timeout 3 bash -c "echo > /dev/tcp/$ANDROID_ADB_SERVER_ADDRESS/5037" 2>/dev/null; then
    return 0
  fi
  echo ">> adb server not running; starting Windows adb ($WIN_ADB)..."
  if [ ! -x "$WIN_ADB" ]; then
    echo "!! $WIN_ADB not found. Set WIN_ADB to the Windows adb.exe path."
    exit 1
  fi
  "$WIN_ADB" start-server
}

# Prints one adb serial per physical device. A Wi-Fi phone shows up twice
# (ip:port + mDNS "adb-<serial>-..._adb-tls-connect._tcp"); both report the same
# hardware serial (ro.serialno — `adb get-serialno` just echoes the transport name),
# so keep only the first entry per serial.
unique_devices() {
  local d serial
  declare -A seen=()
  while read -r d; do
    # </dev/null: otherwise `adb shell` swallows the rest of the loop's input
    serial="$(adb -s "$d" shell getprop ro.serialno </dev/null 2>/dev/null | tr -d '\r')"
    [ -n "$serial" ] || serial="$d"
    if [ -z "${seen[$serial]:-}" ]; then
      seen[$serial]=1
      echo "$d"
    fi
  done < <(adb devices | awk 'NR>1 && $2=="device"{print $1}' | sort)
}
