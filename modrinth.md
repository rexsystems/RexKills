# RexKills

Custom death messages for Paper, Purpur, and Folia. Write a line for every way a player can die, pick who sees it, and reload without a restart.

**Java 21. Paper, Purpur, or Folia 1.21+.** PlaceholderAPI is optional.

## Features

- One message per damage cause, with a default when a cause has no line
- MiniMessage, `&` codes, `&#RRGGBB`, and `#RRGGBB` in every player-facing string
- Send the chat line to everyone, the world, a radius, the victim and killer, or just one of them
- Optional title, action bar, boss bar, and sound, each with its own audience
- World allow list and deny list
- Bypass permission keeps the vanilla message for that player
- Preview any cause with `/rexkills preview`
- Placeholders for the last cause, killer, and weapon

## Commands

| Command | Description | Permission |
|---|---|---|
| `/rexkills help` | Show help | everyone |
| `/rexkills reload` | Reload `config.yml` | `rexkills.reload` |
| `/rexkills preview <cause> [killer]` | Print one death line to yourself | `rexkills.preview` |

Aliases: `/rxk`, `/kills`

## Permissions

| Permission | Default | Description |
|---|---|---|
| `rexkills.reload` | op | Reload the configuration |
| `rexkills.preview` | op | Preview a death message |
| `rexkills.bypass` | op | Keep the vanilla death message |
| `rexkills.custom` | true | Used only when `settings.require-permission` is true |

## Placeholders

| Placeholder | Description |
|---|---|
| `%rexkills_last_cause%` | Last damage cause for that player |
| `%rexkills_last_killer%` | Last killer name, empty when there was none |
| `%rexkills_last_weapon%` | Last weapon name, empty when there was none |

These reset when the server restarts.

Inside `config.yml`, death lines use `{player}`, `{killer}`, `{weapon}`, `{cause}`, `{world}`, `{x}`, `{y}`, and `{z}`.

Documentation: https://docs.rexsystems.me/rexkills/
