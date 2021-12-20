# French Vanilla

A simple server-side only vanilla+ mod for Minecraft using Fabric.

This mod can also be installed on clients for use in single-player worlds.

## Dependencies

- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

### Optional

- Permissions provider (e.g. LuckPerms)

## Features

- Armour swapping
    - Right click with armour or elytra to replace the armour you're currently wearing
    - Can be enabled per-player
- Commands
    - `/deaths` - See Death locks
    - `/fv_config`
        - Change config settings
        - Permission: `frenchvanilla.config` or permission-level 4
    - `/my_config` - See Player config
    - `/tpto`, `/tptome`, `/tpa` - See Teleport commands
- Carrying buckets
    - Backpack with 9 slots
    - Crafted by surrounding a bucket with 4 leather in a plus shape
- Death locks
    - Dying creates a new death lock
    - A player gets a death key on respawn or through the `/deaths` command
    - When used, the key either tells the player where they died, or if they're close enough, it gives them their items
      back
- Homes
    - Use `/sethome` to set your home
    - Use `/home` to teleport to your home
    - Operators can teleport to other players' homes
- Ore miner
    - Break an ore block while holding the ore mine key to break the entire vein
- Player config
    - Per-Player config
    - Use `/my_config` to see your settings
    - Modify each setting with `/my_config <setting> <new_value>`
- Rest at campfires
    - With an empty hand, right-click a campfire placed next to a bed to skip to night
- Teleport commands
    - `/tpto <player>` - request to teleport command user to player
        - Permission: `frenchvanilla.teleport` or permission-level 2
    - `/tptome <player>` - request a player to teleport to command user
        - Permission: `frenchvanilla.teleport` or permission-level 2
    - `/tpa` - accept a teleport request if there is only one
    - `/tpa <player>` - accept a teleport request from a specific player
- Timber
    - Break a log on a tree to break the entire tree
    - Works on nether trees
    - Only works on logs connected to naturally generated leaves (for overworld trees)
    - Can be enabled per-player

Each feature can be disabled using the config.