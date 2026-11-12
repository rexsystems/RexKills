---
title: Messages
description: Prefix, help text, and the colors RexKills accepts.
sidebar:
  order: 2
---

Every string under `messages`, plus titles, the action bar, the boss bar, and the death screen, goes through the same parser.

You can mix all of these in one line:

- MiniMessage: `<gray>`, `<red>`, `<bold>`, `<gradient:#ff0000:#00ff00>`
- Legacy: `&a`, `&l`
- Hex: `&#ff0000` or `#ff0000`

`{prefix}` inserts `messages.prefix`.

A broken MiniMessage tag does not stop the message. That line falls back to legacy codes.

## Command text

| Key | When it is sent |
|---|---|
| `messages.help-header` | First line of `/rexkills help` |
| `messages.help` | The rest of the help, one list entry per line |
| `messages.reload` | After a successful reload |
| `messages.no-permission` | Missing permission |
| `messages.players-only` | Console tried to preview |
| `messages.preview-usage` | `/rexkills preview` without a cause |

## Names inside a death line

`{player}`, `{killer}`, and `{weapon}` are inserted as plain text. A name that contains `<` or `&` cannot recolor the rest of the line. Put the color in the template, around the token.

`messages.unknown-killer` is used when nothing killed the player. The default is `something`.

`messages.unknown-weapon` is used when there is no held item and no projectile. The default is empty.
