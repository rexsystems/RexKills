---
title: RexKills
description: Custom death messages for Paper, Purpur, and Folia.
---

RexKills replaces the vanilla death line with one you write. Each damage cause can have its own sentence. Chat, title, action bar, boss bar, and sound are separate, and each one has its own audience.

Messages use MiniMessage. Legacy `&` codes, `&#RRGGBB`, and `#RRGGBB` also work.

## Files

```
plugins/RexKills/
  config.yml
```

A reload that changes the file writes `config.yml.bak.<timestamp>` first, then adds any keys that were missing. Values you already set stay as they are.

## Next

- [Installation](/rexkills/getting-started/installation/)
- [Death messages](/rexkills/configuration/death-messages/)
- [Commands](/rexkills/reference/commands/)
