# Dynamic ambient sound assets

This folder contains Minecraft-ready `.ogg` variants converted from the local Sounds of Skyrim Complete SE Full profile provided at:

`/Users/bowieconnors/Downloads/Sounds of Skyrim Complete SE-8286-3-1-0-1758715130`

The dynamic ambient client system plays these sounds based on biome, time of day, weather, nearby water, interiors, caves, ruins, and settlement-like blocks.

Each registered sound event has a base file plus numbered variants, for example:

- `forest_birds_day.ogg`
- `forest_birds_day_02.ogg`
- `forest_birds_day_03.ogg`

`sounds.json` lists the variants explicitly, so Minecraft randomly picks one when a matching event is played. The source-file mapping is documented in `SOUNDS_OF_SKYRIM_SOURCE_MAP.md`.

Source mod reference:
https://www.nexusmods.com/skyrimspecialedition/mods/8286

If this project is redistributed, verify the Sounds of Skyrim permissions and third-party source credits for the included audio files.
