# Tarot Reader

Alternate layout using Room plugin + KSP, DeckRepository pattern, and DrawnCard model.

## Open in Android Studio
1. File > Open... and select this folder.
2. Let Gradle sync (JDK 17).

## Notes
- Shuffle logic lives in `repo/DeckRepository.kt` and returns `DrawnCard` entries.
- `ReadingViewModel` maps to actual card data and feeds the Interpreter.
- Replace `assets/deck.json` with your full deck and add images under `assets/cards/`.

Generated 2025-11-06T05:40:59.217733
