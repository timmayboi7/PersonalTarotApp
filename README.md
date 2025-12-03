# Tarot Reader

A minimal, offline-first tarot reader built with Kotlin + Jetpack Compose.

## What's included
- Compose navigation (Home, Spread Picker, Reading)
- Deterministic daily seed + fair shuffle
- Minimal deck (sample cards) and spreads in `assets/`
- Stub Interpreter that composes a readable paragraph
- Ready for expansion (Room/DataStore in place)

## Open in Android Studio
1. File > Open... and select this folder.
2. Let Gradle sync. Use JDK 17 if prompted.
3. Run on a device/emulator (minSdk 26).

## Extend
- Replace `assets/deck.json` with your full 78-card set.
- Add images under `app/src/main/assets/cards/` and reference in `imageAsset`.
- Enhance `Interpreter.kt` rules; add Journal and Share-as-image next.

Generated 2025-11-06T05:31:37.785436
