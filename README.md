# RexKills

Custom death messages for Paper, Purpur, and Folia. Each damage cause has its own line, and you choose who hears it.

<p>
  <img src="https://img.shields.io/badge/Minecraft-1.21%2B-62B47A?style=for-the-badge" alt="Minecraft 1.21+">
  <img src="https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
</p>

## Features

- A message for every damage cause, plus a default
- MiniMessage, legacy `&` codes, `&#RRGGBB`, and `#RRGGBB`
- Audience: everyone, the world, a radius, the people involved, the victim, or the killer
- Optional title, subtitle, action bar, boss bar, and sound
- World allow and deny lists
- PlaceholderAPI, including the last cause, killer, and weapon
- `/rexkills preview` so you can read a line without dying

## Requirements

- Java 21
- Paper, Purpur, or Folia 1.21+
- PlaceholderAPI is optional

## Install

1. Put the jar in `plugins/`
2. Restart
3. Edit `plugins/RexKills/config.yml`
4. Run `/rexkills reload`

## Commands

| Command | Permission |
|---|---|
| `/rexkills help` | everyone |
| `/rexkills reload` | `rexkills.reload` |
| `/rexkills preview <cause> [killer]` | `rexkills.preview` |

Aliases: `/rxk`, `/kills`

## Documentation

The pages in [docs/](docs/index.md) drop into a Starlight site at `src/content/docs/rexkills/`. Paste `docs/sidebar.mjs` into the site sidebar.

Full docs: [https://docs.rexsystems.me/rexkills/](https://docs.rexsystems.me/rexkills/)

## License

Licensed under the [GNU Affero General Public License v3.0](LICENSE).
