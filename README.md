# French Vanilla

A simple server-side only vanilla+ mod for Minecraft using Fabric.

This mod can also be installed on clients for use in single-player worlds.

## Dependencies

- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## Features

- Armour swapping
    - Right click with armour or elytra to replace the armour you're currently wearing
    - Can be enabled per-player
- Carrying buckets
    - Backpack with 9 slots
    - Crafted by surrounding a bucket with 4 leather in a plus shape
- Death Locks
    - Dying creates a new death lock
    - A player gets a death key on respawn or through the /deaths command
    - When used, the key either tells the player where they died, or if they're close enough, it gives them their items
      back
- Homes
    - Use /sethome to set your home
    - Use /home to teleport to your home
    - Operators can teleport to other players' homes
- Player Settings
    - Per-Player settings
    - Use /my_settings to see your settings
    - Modify each setting with /my_setting <setting> <new_value>
- Rest at campfires
    - With an empty hand, right-click a campfire placed next to a bed to skip to night
- Timber
    - Break a log on a tree to break the entire tree
    - Works on nether trees
    - Only works on logs connected to naturally generated leaves (for overworld trees)
    - Can be enabled per-player

Each feature can be disabled using the config.