# Personal Tarot Reader – Shareable Overview

## What the app does
- Presents a Jetpack Compose single-activity tarot experience with three destinations: Home (daily card), Spread Picker, and Reading.
- Draws cards from bundled JSON assets and lets users reveal each card in a spread, including upright/reversed meanings and an auto-generated interpretation.

## Architecture at a glance
- **Entry point:** `MainActivity` applies `TarotReaderTheme` and hosts `TarotAppRoot`, which wires the navigation graph for Home → Spread Picker → Reading.
- **Dependency injection:** Hilt injects repositories and view models across the app.
- **State management:** Each screen has a view model exposing sealed UI states (`Loading`, `Error`, `Ready/Result`) backed by coroutines and `StateFlow`.

## Data sources
- **Card catalog:** `CardStore` lazily loads and caches the full deck from `assets/deck.json` using Kotlin serialization. Cards include arcana/suit, upright/reversed keywords and meanings, and an image filename.
- **Spread catalog:** `SpreadRepository` lazily loads/caches spreads from `assets/spreads.json`, exposing helper lookup by ID (defaulting to the first spread if missing).
- **Drawing logic:** `DeckRepository` shuffles the cached deck with a seedable `Random`, draws the requested count, and pairs each card with a deterministic reversed flag derived from a secondary salted RNG.

## Randomness and determinism
- `TarotRng` centralizes seeding: `secureSeed()` for one-off readings, `dailySeed(zone)` for stable daily draws based on the current date, and `random(seed)` helpers.
- Home screen uses the date-based seed for both card selection and upright/reversed orientation, ensuring every device sees the same daily card for that day/time zone.

## Screen flows
- **Home**: fetches the daily card on first composition, showing progress, error text, or the card details (name, reversed badge, keywords, and meaning). Provides a button to start a reading.
- **Spread Picker**: lists available spreads, displaying each spread’s name and ordered position labels. Selecting a spread navigates to the Reading screen with that spread ID.
- **Reading**: kicks off a fresh draw for the chosen spread, then renders a vertically scrolling list of cards. Each card starts face-down, can be tapped to flip, and shows position notes, upright/reversed meaning text, and a gradient overlay on the artwork. A prose interpretation appears at the end, summarizing suit/arcana dominance and per-position notes.

## Interpretation pipeline
1. `ReadingViewModel` requests the spread and draws cards with a secure seed.
2. `Interpreter.compose` builds a summary noting major arcana dominance and the most common suit, then lists each card’s meaning (favoring upright/reversed meanings or keywords when meanings are blank).

## Assets to supply
- Ensure `app/src/main/assets/deck.json` and `app/src/main/assets/spreads.json` exist with valid JSON matching the `TarotCard` and `Spread` schemas.
- Place card art PNGs under `app/src/main/assets/cards/`, referenced by the `imageAsset` field; the UI also uses `cards/Card Back.png` for the hidden state.

## How to run
1. Open the project in Android Studio with JDK 17.
2. Let Gradle sync; Hilt + Compose are already configured.
3. Run the `app` configuration on an emulator or device to interact with the daily card, pick spreads, and perform readings.
