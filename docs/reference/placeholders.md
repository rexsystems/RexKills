---
title: Placeholders
description: PlaceholderAPI tokens and the tokens inside config.yml.
sidebar:
  order: 3
---

PlaceholderAPI has to be installed before RexKills, or loaded early enough that it is present when RexKills enables. Restart after adding it.

| Placeholder | Description |
|---|---|
| `%rexkills_last_cause%` | Last damage cause name |
| `%rexkills_last_killer%` | Last killer, or empty |
| `%rexkills_last_weapon%` | Last weapon, or empty |

They describe the last death RexKills actually handled for that player. A bypassed death, a disabled world, or a death while the plugin is disabled does not update them. They are kept in memory and reset on restart.

## Inside config.yml

`{player}`, `{killer}`, `{weapon}`, `{cause}`, `{world}`, `{x}`, `{y}`, `{z}`, and `{prefix}` are RexKills tokens.

Other `%placeholder%` values are passed to PlaceholderAPI as the player who died, so `%player_name%` in a death line is the victim. `%player%` on its own is the RexKills token, not a PlaceholderAPI key. `%player_health%` is left for PlaceholderAPI.
