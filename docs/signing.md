### 1. Verify the tag

  cd /home/blitz/ws/coffee-log

  git rev-parse v1.0.0

  It should output:

  76e689ad3520a9af716725975d4c85097ac8207f

  ### 2. Create a clean checkout of the tag

  Your working tree has unrelated changes, so build from a temporary worktree:

  git worktree add /tmp/coffee-log-v1.0.0 v1.0.0
  cd /tmp/coffee-log-v1.0.0

  ### 3. Build the release APK

  ./gradlew clean :app:assembleRelease

  Find the generated APK:

  find app/build/outputs/apk/release -name '*.apk' -type f

  It will probably be:

  app/build/outputs/apk/release/app-release-unsigned.apk

  ### 4. Create a release signing key

  Only do this if you do not already have an Android signing key:

  mkdir -p /home/blitz/keys

  keytool -genkeypair \
    -keystore /home/blitz/keys/coffee-log-release.jks \
    -alias coffee-log \
    -keyalg RSA \
    -keysize 4096 \
    -validity 10000

  Keep this keystore and its passwords safe. Never commit it to Git.

  ### 5. Sign the APK

  apksigner sign \
    --ks /home/blitz/keys/coffee-log-release.jks \
    --ks-key-alias coffee-log \
    --out app-release.apk \
    app/build/outputs/apk/release/app-release-unsigned.apk

  Verify it:

  apksigner verify --verbose app-release.apk

  If apksigner is not found, it is inside your Android SDK’s build-tools directory.

  ### 6. Get the signing certificate fingerprint

  apksigner verify --print-certs app-release.apk

  Copy the Signer #1 certificate SHA-256 digest. Remove the colons and convert it to lowercase.

  Example:

  AllowedAPKSigningKeys: 86fc9c9cde4c0c57bcf5766e2a103c7cd1ff9db7eee4643a2159c6ecae4d48f6

  ### 7. Create a GitHub release

  On GitHub:

  1. Open https://github.com/adrian-2414745/coffee-log
  2. Select Releases
  3. Select Draft a new release
  4. Choose the existing tag v1.0.0

  The final APK URL will be:

  https://github.com/adrian-2414745/coffee-log/releases/download/v1.0.0/app-release.apk

  ### 8. Update your F-Droid metadata

  Add these fields before Builds::

  Binaries: https://github.com/adrian-2414745/coffee-log/releases/download/v%v/app-release.apk

  For version 1.0.0, %v becomes 1.0.0.

  F-Droid will then compare its build with your APK. If they do not match exactly, it will reject the upstream binary. F-Droid documents this
  verification process here. (https://f-droid.org/docs/Build_Metadata_Reference/#binaries)

  If you only want F-Droid to build and sign the application, skip all signing steps and leave Binaries out.