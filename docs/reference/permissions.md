---
title: Permissions
description: Permission nodes shipped with RexKills.
sidebar:
  order: 2
---

| Permission | Default | Description |
|---|---|---|
| `rexkills.reload` | op | Reload the configuration |
| `rexkills.preview` | op | Preview a death message |
| `rexkills.bypass` | op | This player's deaths keep the vanilla message |
| `rexkills.custom` | true | Required only when `settings.require-permission` is true |

There is no permission for seeing someone else's death message. Who sees a line is `settings.audience`, or the `to` key on a title, sound, action bar, or boss bar.
