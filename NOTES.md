## Recent Changes

- **Daily card flow**: Moved daily draw off the home screen; home now has a button to open a fullscreen daily card with mystical framing, description, and close action. Typography updated (cursive display, larger sizes).

- **Spread picker redesign**: Replaced the list with deck-style selectors (Single, 3-Card, 5-Card, Advanced) and a carousel/fan interaction. Added spread data for Yes/No/Maybe, Mind/Body/Spirit, Cross (5), Rectangle (5), Celtic Cross, Compatibility H, Horseshoe. Decks defined in `SpreadPickerScreen.kt` via `deckDefinitions()`; spreads data in `app/src/main/assets/spreads.json`.

- **Spread layouts**: Reading screen now positions cards per spread shape:
  - Three-card variants: row.
  - Cross Five: center/north/east/south/west cross.
  - Rectangle Five: upper-left, lower-left, center, upper-right, lower-right.
  - Celtic Cross: dedicated layout.
  - Horseshoe: arcing 7-card curve.
  - Compatibility H: “H” frame layout.
  - Others fall back to horizontal list.
  Cards on the table no longer show labels.

- **Fullscreen card overlay**: Only fullscreen shows plaques. Top plaque shows spread position; bottom plaque uses a custom ornate SVG (`app/src/main/assets/title_block.svg`). Added a dark translucent backdrop behind the plaque, with 28sp medium-weight gold title. Artwork remains clean on the table. 
  - Coil SVG support added: `io.coil-kt:coil-svg:2.7.0` and a `svgLoader` in `FullscreenCardOverlay`.

- **Centering**: Spread content on the reading screen is vertically centered using a weighted box; extra padding added.

## Files Touched
- `app/src/main/java/com/timmay/tarot/ui/screens/HomeScreen.kt`: daily card button → fullscreen.
- `app/src/main/java/com/timmay/tarot/ui/screens/SpreadPickerScreen.kt`: deck carousel UI.
- `app/src/main/java/com/timmay/tarot/ui/screens/ReadingScreen.kt`: spread layouts, fullscreen plaques, centering, title SVG.
- `app/src/main/assets/spreads.json`: added new spreads.
- `app/build.gradle.kts`: added `coil-svg`.
- `app/src/main/assets/title_block.svg`: ornate title border asset (moved from drawable).
- `app/src/main/java/com/timmay/tarot/ui/theme/Type.kt`: expressive fonts and larger sizes.

## Follow-ups / Tweaks
- Adjust plaque backdrop opacity or colors if readability needs tuning.
- Fine-tune spread offsets (especially horseshoe/rectangle) per on-device feel.
- Add snapping to deck carousel if desired.
- If IDE screenshots are needed in assets, use `adb exec-out screencap -p > app/src/main/assets/<name>.png` (Studio’s tool doesn’t honor project settings).
