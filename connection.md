# Coffee Log — Connecting a Phone over Wi-Fi

Wireless ADB debugging for the real phone (Samsung Galaxy A52s 5G, `SM-A528B`),
built in WSL2 and reached through the Windows adb server over shared `localhost`
(mirrored networking — same bridge the emulator uses). No USB, no usbipd.

See `environment-setup.md` for the overall hybrid setup and the USB path.

## Phone facts

- **Static IP:** `192.168.50.43` (reserved on the router — never changes)
- Network name: `MyAsusLaptop`
- Model: `SM-A528B` (Galaxy A52s 5G)

## First-time pairing (once per machine)

Android 11+ "Wireless debugging" requires a one-time pairing before `adb connect`
works. Port `5555` is refused — the modern flow uses random ports.

1. On the phone: **Settings → System → Developer options → Wireless debugging** → enable.
2. Tap **"Pair device with pairing code"**. The popup shows a **pairing IP:port**
   (random) and a **6-digit code**. Keep it open — the code expires on close.
3. From WSL:
   ```bash
   adb pair 192.168.50.43:<pair-port> <6-digit-code>
   ```
   Pairing **persists** across reboots/toggles — you only do this once per machine
   (unless you revoke it on the phone).

## Connecting (every session)

The **connect port** is different from the pairing port and is shown on the *main*
Wireless debugging screen under **"IP address & Port"**. It changes every time you
toggle Wireless debugging off/on or reboot the phone.

```bash
./adb-wifi.sh <connect-port>      # e.g. ./adb-wifi.sh 37621
```

Then build + install + launch on all connected devices (phone + emulator):

```bash
./dev.sh
```

## Notes / troubleshooting

- **Only the port moves.** Static IP + persistent pairing mean each session is just
  one `adb connect` with the new port.
- mDNS auto-discovery (`adb mdns services`) comes up empty across the WSL/Windows
  bridge — that's expected; use the explicit port instead.
- If the phone silently drops off `adb devices` after idle, just re-run
  `./adb-wifi.sh <port>`.
- Fallback (no pairing dance): plug in via USB once, run `adb tcpip 5555`, then
  `adb connect 192.168.50.43:5555` works on a fixed port.

## Worked example (2026-07-04)

```
adb pair 192.168.50.43:41645 072466      # -> Successfully paired
adb connect 192.168.50.43:37621          # -> connected
./dev.sh                                 # -> installed + launched on phone + emulator
```
