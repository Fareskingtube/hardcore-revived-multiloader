[//]: # (TODO: Add Stats from cursefore like in https://github.com/cubex2/chesttransporter/blob/master/README.md)

# Hardcore Revived

This is a fabric minecraft mod that tries to enhance the multiplayer hardcore experience by allowing you to revive your
dead
friends in a **Revival Altar** by **sacrificing** one of your **hearts**.

# What the mod adds

This mod allows you to revive your friends in hardcore by building a multiblock **(Revival Altar)** and placing your
heart in it.

## The Revival Altar

The Revival Altar is the center of this mod it has 2 main functions:

[//]: # (TODO: Edit README.md to reflect the new revival system)

1. It passively gives you regeneration while within its range.
2. Allows you to revive your friends by placing a heart in it then getting your friend who's in spectator mode within
   its range.

## The Altar

[![Revival Altar Video](https://i.ibb.co/ynv4TLf5/image.png)](https://youtu.be/oQ9XKFr5kiY)

## Revival

[![Revival Altar Revival Video](https://i.ibb.co/BVvqgx4y/2026-08-06-14-42-15.png)](https://youtu.be/zcmzzAZ1ubY)

## Hardcore Heart

This item is the physical manifestation of your heart in your hand.
It can be obtained via **heart extractor**.
Once you have a heart it can be reinjected via **Heart Injector**,
gifted to a friend or thrown into lava.

[![Revival Altar Video](https://i.ibb.co/fYWgTYm4/2026-08-06-14-45-45.png)](https://youtu.be/Y6KFRJC33Tc)

## Resuscitatio Mortuorum

This is the in game guide book that shows you what each item does how to get it.
Also shows you a framework on how to build the [Altar Multiblock](#the-revival-altar) as shown in
the [provided video](#the-altar).

You should spawn with it when making a new world, but you can also craft it with a **book** and a **blood block**.

# Requirements

## Version

Currently Hardcore Revived only works on version 1.21.1 of minecraft.

## Mod Loader

Hardcore Revived only supports the [Fabric Mod Loader](https://fabricmc.net/) and
requires [Fabric API](https://github.com/FabricMC/fabric-api).

## Dependencies

These are mods you must have in your `mods/` folder for the mod to work correctly:

[//]: # (TODO: Add diffrent links based on the platform)

* [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api/)
  Version [0.116.12+1.21.1](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/8073321) or higher.
* [Patchouli (Fabric)](https://www.curseforge.com/minecraft/mc-mods/patchouli-fabric)
  Version [1.21.1-93](https://www.curseforge.com/minecraft/mc-mods/patchouli-fabric/files/7730941) or higher.
* [YACL](https://www.curseforge.com/minecraft/mc-mods/yacl)
  Version [3.8.2](https://www.curseforge.com/minecraft/mc-mods/yacl/files/7437855) or higher.

## Integration

* [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) Adds a mods list screen and a screen to edit the
  config in game.

## Incompatibility

This mod is incompatible with some mod that modifies your `MAX_HEALTH` player attribute.

Known incompatibilities:

* [Apotheosis](https://www.curseforge.com/minecraft/mc-mods/apotheosis)

Any mod that allows you to increase then increase your `MAX_HEALTH` player attribute at will allows for farming hearts
for infinite health.
Any mod that **only** allows you to only increase or **only** decrease your `MAX_HEALTH` player attribute **should** be
fine.

# Installation

To install the mod you just need to put the `.jar` file in your
`YOUR_MINECRAFT_INSTANCE/mods/` folder with the [Dependency Mods](#dependencies) on a [Fabric](https://fabricmc.net/)
installation.

# AI usage

AI was usage was mostly limited to debugging and explaining some concepts/finding documentation since up-to-date
documentation for mod development is fairly limited.

The **known** places where generative was used to write code:

1. Spawning particle methods at `RevivalAltarBlockentity.java`
2. Fetching and Caching offline player skins at
   `client/PlayerProfileTextureCache.java`
3. Small help with styling (Centering a div) at `client/PlayerSelectorScreen.java`

**Note:** Any "borrowed" code has its source above it.

# Credits

## Authors

1. **[![](https://minotar.net/helm/d6313fc2-7814-4de1-8326-a468c509d038/15)](https://namemc.com/profile/d6313fc2-7814-4de1-8326-a468c509d038)[Fareskingtube](https://github.com/Fareskingtube) (
   Main & Only Dev, Main Artist, 3D Artist, Builder, etc.)**

2. [![](https://minotar.net/helm/ec20c236-b99c-4d42-a633-dedcc41c4f94/15)](https://namemc.com/profile/ec20c236-b99c-4d42-a633-dedcc41c4f94)
   [Itsbluefire_](https://namemc.com/profile/ec20c236-b99c-4d42-a633-dedcc41c4f94) (Builder)
3. [![](https://minotar.net/helm/33e2ca4f-ba29-4dd3-b9f2-092a8a13ba21/15)](https://namemc.com/profile/33e2ca4f-ba29-4dd3-b9f2-092a8a13ba21)
   [Oddsyy](https://namemc.com/profile/33e2ca4f-ba29-4dd3-b9f2-092a8a13ba21) (Builder)

## Assets

The following assets were "borrowed" from the following sources:

* **Heart Injector** texture was taken
  from [RFTools](https://github.com/McJtyMods/RFTools/blob/1.12/src/main/resources/assets/rftools/textures/items/syringeitem0.png)
  under the [MIT License](https://github.com/McJtyMods/RFTools/blob/1.12/LICENSE.md).
* **Heart Extractor** texture was taken
  from [Chest Transporter](https://github.com/cubex2/chesttransporter/blob/master/src/main/resources/assets/chesttransporter/textures/items/ct_wood.png)
  under the [GPL-3.0 License](https://github.com/cubex2/chesttransporter/blob/master/LICENSE)

The assets above are temporary and for testing proposes only.

* **Blood Block** texture was taken and scaled up
  from [unused-textures](https://github.com/malcolmriley/unused-textures/blob/master/blocks/cheese_waxed_bottom.png)
  under the [CC-BY-4.0 License](https://github.com/malcolmriley/unused-textures/blob/master/LICENSE) (Also used for
  the altar)

Any other assets were made entirely by the [authors](#authors).

# Modpack Policy

**You're allowed to use this mod in your modpack. You don't have to ask permission.**

[//]: # (TODO: Add Discord Link)
Although I would be interested in knowing what you're making. Take a look at the [discord]() if you need my help or
want to show me your projects.


