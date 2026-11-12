---
title: Death messages
description: Cause keys, placeholders, and who the killer and weapon are.
sidebar:
  order: 3
---

`messages.default` is used when `messages.causes` has no line for that cause, or the line is blank.

Cause keys are Bukkit damage causes. Write them in any case. The plugin stores them in upper case.

`CONTACT`, `ENTITY_ATTACK`, `ENTITY_SWEEP_ATTACK`, `PROJECTILE`, `SUFFOCATION`, `FALL`, `FIRE`, `FIRE_TICK`, `MELTING`, `LAVA`, `DROWNING`, `BLOCK_EXPLOSION`, `ENTITY_EXPLOSION`, `VOID`, `LIGHTNING`, `SUICIDE`, `STARVATION`, `POISON`, `MAGIC`, `WITHER`, `FALLING_BLOCK`, `THORNS`, `DRAGON_BREATH`, `CUSTOM`, `FLY_INTO_WALL`, `HOT_FLOOR`, `CAMPFIRE`, `CRAMMING`, `DRYOUT`, `FREEZE`, `SONIC_BOOM`, `KILL`, `WORLD_BORDER`.

A cause that does not exist yet still falls back to `messages.default`. Add a key with that exact name and reload.

## Placeholders

| Token | Value |
|---|---|
| `{player}` | The player who died |
| `{killer}` | Player killer, projectile owner, or mob name. Otherwise `messages.unknown-killer` |
| `{weapon}` | Projectile name, or the main-hand item. Otherwise `messages.unknown-weapon` |
| `{cause}` | The damage cause, such as `FALL` |
| `{world}` | World name |
| `{x}` `{y}` `{z}` | Block coordinates of the death |

A renamed item uses its display name. A renamed projectile uses its custom name. Other items and mobs use a readable type name, so `DIAMOND_SWORD` becomes `Diamond Sword`.

Arrows and other projectiles use the shooter as `{killer}`. A player shooter is that player's name.

`/kill` and deaths with no last damage use the cause `CUSTOM`.

## Preview

`/rexkills preview FALL` prints the fall line to you. `{player}` is you. Add a killer name as the next word:

```
/rexkills preview ENTITY_ATTACK Steve
```

The weapon in a preview is whatever you are holding.
