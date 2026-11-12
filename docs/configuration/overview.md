---
title: Configuration
description: The settings, audiences, and feedback channels in config.yml.
sidebar:
  order: 1
---

`config.yml` is the only file. `config-version` is internal. Leave it alone.

## settings

| Key | What it does |
|---|---|
| `enabled` | Master switch. `false` leaves every death vanilla. |
| `audience` | Who receives the chat line. `all`, `world`, `radius`, `involved`, `victim`, or `killer`. |
| `radius` | Blocks, used when an audience is `radius`. `0` means the whole world. Values above 256 are clamped to 256. |
| `cancel-vanilla` | When `true`, the vanilla chat line is removed. A limited audience always removes it, even when this is `false`. |
| `death-screen` | `keep`, `custom`, or `hide`. See [Requirements](/rexkills/getting-started/requirements/). |
| `vanilla-death-sound` | `false` stops the vanilla death sound. |
| `require-permission` | When `true`, only players with `rexkills.custom` get a custom message. |
| `use-display-name` | `{player}` and a player `{killer}` use the display name. |

`rexkills.bypass` always keeps the vanilla message for that player's own death, before any of the settings above.

## worlds

If `worlds.enabled` has any names, custom messages run only there. Otherwise `worlds.disabled` is a deny list. Both empty means every world.

## sound, title, actionbar, bossbar

Each block has `enabled` and `to`. `to` uses the same audience names as `settings.audience`.

An empty `sound.sound` disables the sound even when `sound.enabled` is true. Names are Bukkit sound enum names, such as `ENTITY_PLAYER_DEATH`. An unknown name is logged once.

Title times (`fade-in`, `stay`, `fade-out`) are ticks. 20 ticks is one second.

Boss bar `seconds` is clamped between 1 and 60. `color` is `PINK`, `BLUE`, `RED`, `GREEN`, `YELLOW`, `PURPLE`, or `WHITE`. `style` is `SOLID`, `SEGMENTED_6`, `SEGMENTED_10`, `SEGMENTED_12`, or `SEGMENTED_20`.

`death-screen.message` is used only when `settings.death-screen` is `custom`.

Reload applies the next death. A death that is already being announced keeps the text it started with.
