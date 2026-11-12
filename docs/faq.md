---
title: FAQ
description: Why a death message did not change.
sidebar:
  order: 1
---

## The vanilla line is still there

Check these in order:

1. `settings.enabled` is true.
2. The player does not have `rexkills.bypass`.
3. The world is allowed. A non-empty `worlds.enabled` list ignores every other world.
4. `settings.require-permission` is false, or the player has `rexkills.custom`.
5. The cause line and `messages.default` are not blank.
6. `settings.audience` is `all` and `settings.cancel-vanilla` is false. That mode hands the line to vanilla, which also broadcasts it. Any other audience removes the vanilla line and sends RexKills' own.

Another plugin that listens at a later priority can still replace the chat line after RexKills.

## The respawn screen still says "You died!"

`settings.death-screen` only runs on Paper builds that have a death-screen method. Paper 1.21.1 does not. The chat message is separate and still changes.

## A radius broadcast reaches the whole world

`settings.radius` of `0` means the whole world. A number above 256 is treated as 256.

## Reload did not change a death that just happened

Reload reads the files for the next death. It does not edit a message that is already going out.

## `%rexkills_last_killer%` is empty after a restart

Last death data is kept in memory only.
