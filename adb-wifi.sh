#!/usr/bin/env bash
# Reconnect the real phone over Wi-Fi ADB.
#
# Pairing is one-time and persists (see docs/connection.md); only the *connect port*
# changes each session — it's shown on the phone's main Wireless debugging screen
# under "IP address & Port". Pass that port here.
#
#   ./adb-wifi.sh 37621
#
set -euo pipefail

export ANDROID_HOME="${ANDROID_HOME:-$HOME/Android/Sdk}"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
export PATH="$PATH:$ANDROID_HOME/platform-tools"
export ANDROID_ADB_SERVER_ADDRESS="${ANDROID_ADB_SERVER_ADDRESS:-127.0.0.1}"

PHONE_IP="${PHONE_IP:-192.168.50.43}"   # static, reserved on the router
PORT="${1:-}"

if [ -z "$PORT" ]; then
  echo "usage: $0 <connect-port>"
  echo "  (find it on the phone: Settings -> Developer options -> Wireless"
  echo "   debugging -> 'IP address & Port'. Port changes each toggle/reboot.)"
  exit 1
fi

TARGET="$PHONE_IP:$PORT"
echo ">> connecting to $TARGET ..."
adb connect "$TARGET"

echo ">> devices:"
adb devices -l
