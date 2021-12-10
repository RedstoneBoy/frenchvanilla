## Version 1.1.0

- Added dependency: fabric-permissions-api
- Added /fv_config command to change settings
    - Permission: frenchvanilla.config or permission-level 4

## Version 1.0.0

- Added dependencies: NBTCrafting, SGui
- Added carrying buckets
    - Backpacks with 9 slots
    - Crafted by surrounding a bucket with 4 leather in a plus shape

## Version 0.9

- BREAKING: Various components now use different NBT paths, worlds that used any previous version of this mod are now
  incompatible with this version
- Added per-player settings using the /my_settings command

## Version 0.8.1

- Include CompleteConfig

## Version 0.8

- Updated to 1.18
- Death locks can now optionally store xp

### Bug Fixes

- Added some missing translation strings in en_us

## Version 0.7

- Added /sethome and /home
    - Use /sethome to set your home
    - Use /home to teleport to your home
    - Operators can teleport to other players' homes
- Added Timber
    - Break a naturally generated tree log to break the entire tree

### Bug Fixes

- /deaths command can no longer be used when death locks are disabled

## Version 0.6

- Added configuration
- Now requires CompleteConfig
- Added a sound to armor swapping
- Added death locks
    - Dying creates a new death lock
    - When a player respawn, they get a death key for their last death
    - When a player is far from their death, the key tells them where they died
    - When a player is near their death, the key gives them their items back
    - Players can use /deaths to see their deaths, as well as get keys for them

## Version 0.5

- Removed mass storage barrels
- No longer requires NBT Crafting

## Version 0.4

- Now requires NBT Crafting
- Mass barrels must now be created with a mass barrel converter
- Converters are crafted with a nether star surrounded by 8 chests
- Barrels now support hoppers
- Barrels now store 1024 stacks instead of 65536 items
    - For items that stack to 64, this means the barrel still stores 65536 items
    - For non-stackables, the barrel stores 1024 items

## Version 0.3

- Added armour swapping
    - Right click with armour or elytra to replace the armour you're currently wearing
- Mass storage barrels are now created by consuming a diamond, instead of using a diamond hoe
    - Crouch right click a barrel with a diamond to create a mass barrel, using up the diamond

### Bug Fixes

- Barrels with items in them will no longer be converted to mass barrels

## Version 0.2

- Removed ModUpdater support
- Added Mass Storage Barrels
    - Crouch right click a barrel with a diamond hoe to turn it into a mass storage barrel
    - One barrel can hold up to 65536 of the same item
    - Right click with an item to put it in the barrel
    - Crouch right click with an empty hand to transfer all items of the barrels type from your inventory
    - Left click to take one item
    - Crouch left click to take one stack of the item
- Removed translation support since clients without this mod will display untranslated text
    - Looking for a solution

## Version 0.1.3

- Added [ModUpdater](https://gitea.thebrokenrail.com/TheBrokenRail/ModUpdater) support

## Version 0.1.2

- Rest at campfires
    - With an empty hand, right click a campfire placed next to a bed to skip to night