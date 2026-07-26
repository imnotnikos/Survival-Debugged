# Survival Debugged
**Survival Debugged** is a lightweight Bukkit, Paper, and Spigot plugin that brings the power of Minecraft's debug stick to players in survival. Designed for builders and server administrators, this plugin allows users to seamlessly edit the physical properties of blocks—such as rotation, shape, half-states, and open/closed statuses—without needing native Creative mode access or operator permissions.

***
### Plugin made by imNikos.
 
[![Discord](https://img.shields.io/badge/Discord-Nikos'%20Wasteland-5865F2?style=flat-square&logo=discord&logoColor=white)](https://discord.gg/9xJgzNqEaJ)
[![Instagram](https://img.shields.io/badge/Instagram-imNikos_-E4405F?style=flat-square&logo=instagram&logoColor=white)](https://instagram.com/imnikos_)
[![YouTube](https://img.shields.io/badge/YouTube-imNikoss-FF0000?style=flat-square&logo=youtube&logoColor=white)](https://youtube.com/@imnikoss)
[![TikTok](https://img.shields.io/badge/TikTok-imNikos_-000000?style=flat-square&logo=tiktok&logoColor=white)](https://tiktok.com/@imnikos_)
[![GitHub](https://img.shields.io/badge/GitHub-imnotnikos-181717?style=flat-square&logo=github&logoColor=white)](https://github.com/imnotnikos)
[![Modrinth](https://img.shields.io/badge/Modrinth-imNikos-1bd96a?style=flat-square&logo=modrinth&logoColor=white)](https://modrinth.com/user/imNikos)
[![CurseForge](https://img.shields.io/badge/CurseForge-imnotnikos-F16436?style=flat-square&logo=curseforge&logoColor=white)](https://www.curseforge.com/members/imnotnikos/projects)

***


## How It Works
Equipped with the custom debug stick, players can  interact with any block that contains block states (like stairs, doors, fences, and signs).

**Left-Click:** Scans the block and cycles through its available properties (e.g., facing, half, waterlogged).

**Right-Click:** Toggles or cycles through the valid values for the currently selected property.

![Image of the survival debug stick being used on a spruce door.](https://cdn.modrinth.com/data/cached_images/2f44294644cff8765d234dfda07b94f3ca2e873b.png)

## Core Features
**Safe State Calculations:** The plugin dynamically calculates valid block states. When you right-click to change a value, the plugin safely tests the new data behind the scenes. It natively supports toggling booleans (true/false), integer ranges (0-30 for ages and rotations), and cycling through a massive built-in dictionary of over 60 complex string states (such as stair shapes, slab types, wall attachments, and note block instruments).

**Physics & Neighbor Update Prevention:** Modifying blocks with this stick intentionally ignores block physics. This allows builders to achieve "impossible" block states without them instantly breaking or updating—meaning you can edit the top half of a door without it popping off, or force a fence post to disconnect from a neighboring block.

**Per-Player Material Memory:** The stick features a smart memory system. It tracks the last property a player edited on a per-material basis. If you left-click to select the facing property on an Oak Stair, the next Oak Stair you click will automatically default to facing, saving you from having to cycle through the options all over again.

**Clean Action-Bar UI:** To prevent chat spam, all interactions are elegantly displayed via the Action Bar. Players receive color-coded notifications when selecting properties (Yellow), successfully changing values (Green), or attempting to modify a block with no states (Red).

**Secure Item Tracking:** The custom stick is generated using Bukkit's PersistentDataContainer (PDC). This ensures that the debug stick is completely unique and secure—players cannot simply rename a normal stick in an anvil to gain access to these powers.

**Simple Commands:** Server administrators can grant the item using the /sdebug or /debugstick commands (requires the customdebugstick.give permission).

## Download & Installation

* Navigate to the **Releases** tab on the right side of this GitHub repository.
* Download the latest `CustomDebugStick.jar`.
* Drop the `.jar` file into your server's `plugins/` folder.
* Save the file and **restart your server**.
