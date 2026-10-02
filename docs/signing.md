# Signing a release for F-Droid

The F-Droid recipe uses `Binaries:` + `AllowedAPKSigningKeys:`, so F-Droid builds
the tag itself and publishes **our** signed APK only if its build matches ours
byte for byte. Steps per release (example: `1.1.0`):

### 1. Tag and push

Version bumped in `app/build.gradle.kts`, changelog
`fastlane/metadata/android/en-US/changelogs/<versionCode>.txt` added, then:

    git tag -a v1.1.0 -m "1.1.0"
    git push origin main v1.1.0

### 2. Build from a clean clone of the tag

Use a real `git clone`, **not** a `git worktree` — the build must match what
F-Droid gets from its own clone (see the reproducibility note in
`release-to-fdroid.md`).

    git clone ~/ws/coffee-log /tmp/coffee-log-v1.1.0
    cd /tmp/coffee-log-v1.1.0 && git checkout v1.1.0
    ./gradlew clean :app:assembleRelease
    cp app/build/outputs/apk/release/app-release-unsigned.apk ~/ws/coffee-log/

### 3. Sign (you run this — it prompts for the keystore password)

Key: `/home/blitz/keys/coffee-log-release2.jks`, alias `coffee-log`. Never commit
it (`*.jks`, `*.apk`, `*.idsig` are gitignored).

    cd ~/ws/coffee-log
    apksigner sign --ks /home/blitz/keys/coffee-log-release2.jks --ks-key-alias coffee-log \
      --out app-release.apk app-release-unsigned.apk
    apksigner verify --print-certs app-release.apk | grep SHA-256

The SHA-256 must equal `AllowedAPKSigningKeys` in the fdroiddata recipe:
`9782c235b2b3697ddea5b927649ed056429042e9311024c756062008afb1e934`.

### 4. GitHub release

https://github.com/adrian-2414745/coffee-log/releases/new → existing tag
`v1.1.0`, title `1.1.0`, notes from the changelog. Attach the APK named exactly
`app-release.apk` (the recipe's `Binaries:` URL is
`.../releases/download/v%v/app-release.apk`). From Windows the file is at
`\\wsl.localhost\Ubuntu\home\blitz\ws\coffee-log\app-release.apk`.

### 5. Update the F-Droid recipe

In `~/ws/f-droid-data`, `metadata/io.github.adrian2414745.coffeelog.yml`: set the
build's `versionName`/`versionCode`/`commit` (full tag commit hash) and
`CurrentVersion`/`CurrentVersionCode`, then run `fdroid lint`, `fdroid build`
(see `release-to-fdroid.md` step 7). `fdroid build` should log
`compared built binary to supplied reference binary successfully`.
