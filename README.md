# Quests Documentation

A simple quests plugin for Spigot based Minecraft servers.
The plugin does not track completed quests and is item based.
It has permissions features to track quest completion if required. 
This plugin is best used for daily, weekly, monthly etc quests but is easily adaptable allowing for complex quest lines with branching paths, secrets and support for a wide array of objectives.

---

### REQUIRED PLUGINS
- WorldGuard - 7.05+
- Luckperms - any version (not required, just recommended otherwise permissions features wont work)

---

### MINECRAFT VERSION
 - API Compatible - Spigot 1.17.1+
 - Latest tested - 1.20

---

# Commands

## Permissions
- `quest.admin`  
Permission will provide access to all admin, completer and generator commands.
- `quest.<permissionName>.<dateFormat>`  
Permission format for quests stored in `/quest list`.
- `quest.<file>_<quest>`  
Permission format to prevent people from collecting multiple of the same quest. If set and a player has `/gquest` run against them for the specified quest then a new quest wont generate.

## Player
- `/quest help`  
  Lists the 3 player commands if they are not holding a scroll. If they are holding a scroll defined with help lines in the config it will then display those help lines.  
- `/quest claim`  
  Allows a player to claim the reward from a completed quest. This removes the quest scroll in the process. The scroll is required to be in the players main hand to use.  
- `/quest expiry`  
  Checks how long you have left to complete the scroll in your main hand. Returns a number in hours.  
- `/quest secret <code>`  
  Attempts to solve a quest code.  
- `/quest path <num>`  
  Selects a quest path specified on the scroll.  
- `/quest list`  
  Lists all available quests as defined in the `Global.yml` file.

## Completers

These commands are useful to attach to NPCs or run through the console to complete quest lines when run

**The following commands require the quests.admin permission to run.**

**REMEMBER all objectives require the quest to be in your offhand when complete. This applies to running the cquest commands as well.**

- `/cquest txt <playerName> <matchOne> <matchTwo>`  
  This command allows you to complete something on someones scroll such as “Speak To Bob: Incomplete”. 
  All it does is match the first and second arguments to a complete/incomplete line on a quest scroll to complete it. 
  This matching is case insensitive. For example
  `Speak To Bob: Incomplete` could be completed with `/cquest txt <playerName> speak bob`  
- `/cquest num <playerName> <matchOne> <matchTwo> <integer>`  
  Adds the integer value to the line specified. Acts almost identically to cquest txt.  
- `/cquest unlock <playerName> <matchOne> <matchTwo>`  
  Unlocks a line that is specified in the command. Locked lines are ones with a strike through them.

## Admin

**The following commands require the quests.admin permission to run.**

- `/aquest rewardlist`  
  Lists all rewards given out by the scroll in your main hand.  
- `/aquest expiry <TimeInMilliseconds>`  
  Manually sets the expiry for the quest scroll in your main hand.  
- `/aquest claim`  
  Claims the reward of the quest scroll you’re holding regardless of whether or not its complete.  
- `/aquest who`  
  If the scroll can only be completed by a certain person it will tell you who if you hold it in your mainhand.  
- `/aquest reloadlist`  
  Reloads the list config in case you need to make a change while the server is running. Prevents you from having to restart the server.  
- `/aquest version|help`  
  Tells you the version you are running as well as gives you a link to this page.  
- `/aquest auto [true|false]`  
  If true or false is not specified then it returns the current autocomplete status. If true or false is specified then it sets the global autocomplete config option.

## Generator Command

**The following commands require the quests.admin permission to run.**

- `/gquest <configName> <scrollName> <playerName> (force | noverbose)`  
  Gives a quest scroll from the specified config file to the specified player. The scroll will be formatted and contain requirements as defined in the config manually. You can also add force on to the end if the player has the permission set for that scroll and it will bypass any permission restrictions and still give them one which is useful for giving opped players scrolls. Noverbose prevents players receiving the feedback message that they have already collected the quest scroll.

---

# Defining a Scroll

## Basic Requirements
- **Quest Scroll**  
Each scroll MUST have the first line of lore have “Quest Scroll” or “Tracker” in it. Without that they will not track any stats. A quest scroll MUST also be on a piece of paper. Any other item type will not work.

- **Rewards**  
A quest MUST be given a reward otherwise they’re basically just trackers. If a player attempts to claim a quest scroll that does not have a reward set the quest claiming will fail.

- **Something to track**  
For a quest scroll to be claimed it must have something to track and have that be completed. Below I will detail each thing that can be tracked and how they need to be typed out for the tracking to be successful.

- **Quest lines**  
To achieve this you would just set the reward for the first scroll to be the second. The reward for the second to the third etc etc.

## Defining A Scroll In A Config File

Quests are defined in `.yml` files. Within one file you can define multiple quests as seen below.
The general structure for quests starts with a name/id, in the below case `coords1` and `coords2`, to use for `/gquest` and then tabbed after that all of the configuration options.
Below the example config are all available configuration options and their structure.

**Example quest YML file**

```yaml
coords1:
  name: Daily Quest Scroll
  lines:
  - <player>'s Daily Quest Scroll
  - <break>
  - <empty>
  - '&fGo To X1 Y1 Z1: Incomplete'
  - <empty>
  - <break>
  - Use /quest help for quest info
  rewards:
  - gquest coords coords2 <player> force
  autocomplete: true
  UUID: true
  expiry: 86400000
  coords: true
coords2:
  name: A Quest Scroll Name
  lines:
  - <player>'s Quest Scroll
  - <break>
  - <empty>
  - '&fKill 10 Zombies: 0'
  - <empty>
  - <break>
  - Use /quest help for quest info
  rewards:
  - smite <player>
  autocomplete: true
  UUID: true
  expiry: 86400000
```

## Available configurations options

Some options only look to see if any value exists to set the configuration so it is recommended to only include configurations that you want on the scroll.

### Name
**Required**  
Use the name configuration option to set the name of a quest scroll when it is generated. This is the name as seen on the item given to the player.  
Supports colour codes. Defaults to orange/gold.  
**Example:**
```yaml
name: &fExample Name
```

### Lines
**Required**  
The lines configuration option allows you to set the lines of your quest scroll including your objectives. Here you can also set &f, &k and &m. &f sets the line to white with the default being grey, &k scrambles the line so players can’t see certain sections of text and &m puts a strikethrough a line and marks it as locked.  
Default placeholders: `<player>` `<break>` `<empty>` `<date>`  
You can create your own placeholders by adding a list to `Lists.yml` in the quests folder. Then you can reference that list by using `<listName>`. When a list is referenced it will randomly select a line from the list. This can be particularly useful for weekly/daily quests which you want to be randomly generated. You can also use nested lists. Lists will be gone over in more detail below.
You can define number ranges so that a random number is selected every time a quest is generated. For example `[10-20]` will give you any number from 10 to 20.  
**Example:**  
```yaml
lines:
- <player>'s Quest Scroll
- <break>
- <empty>
- '&fKill [10-20] <mobs>: 0'
- <empty>
- <break>
- Use /quest help for quest info
```

### Rewards
**Required**  
The rewards configuration option allows you to set rewards for players when they complete a quest. The rewards are just a list of commands which allows you to run ANY commands when the scroll is completed. If a command requires a playername you can use `<player>` in the configuration line to have it autofill the players name. If you change this in the config it will change the outcome of completing the quest even if the scroll has already been generated.  
**Example:**
```yaml
rewards:
- smite <player>
- give <player> stone 1
```

### Message
**Recommended**  
The message configuration allows you to set a message that is sent to the player when they first collect the quest scroll. This message is only shown when the `/gquest` command is run.  
The colour must be generic Minecraft colour, in capitals, with underscores where applicable. The full list is `BLACK` `DARK_BLUE` `DARK_GREEN` `DARK_AQUA` `DARK RED` `DARK_PURPLE` `GOLD` `GRAY` `DARK_GRAY` `BLUE` `GREEN` `AQUA` `RED` `LIGHT_PURPLE` `YELLOW` `WHITE`  
**Example:**
```yaml
message:
  lines:
  - Blah blah
  - Blah
  name: Applebranch
  colour: BLUE
```

### ClaimMessage
**Recommended**  
The message configuration allows you to set a message that is sent to the player when they claim the quest scroll. This message is only shown when `/quest claim` or `/aquest claim` commands are run successfully.  
The colour must be a generic Minecraft colour as specified just above.  
**Example:**
```yaml
claimMessage:
  lines:
  - Blah blah
  - Blah
  name: Applebranch
  colour: BLUE
```

### Help
**Recommended**  
The help configuration option allows you to set quest specific help and hints. Lines here show up when a player holding the scroll does /quest help. If you change this in the config it will change the outcome of /quest help even if the scroll has already been generated.  
**Example:**
```yaml
help:
- This shows when they do /quest help
- This shows more info when they do /quest help
```

### Locked
If this is defined in any way then the scroll is marked as having locked lines. If the objective above a locked objective is completed, then the objective unlocks and becomes completable. To lock a line use `&m` at the start of the line in question. If you use `&m` but don't define locked in the config then the generated scroll will never be claimable.  
**Example:**
```yaml
locked: true
```

### UUID
**Recommended**  
If this is defined as true then the scroll cannot be completed by anyone other than the person who it was generated for.  
**Example:**
```yaml
uuid: true
```

### Expiry
If this is defined, it sets how long it is until the scroll expires in milliseconds. This allows you to put time limits on quests. The time only starts counting down once the quest is generated and keeps counting down while players are offline.  
**Example:**
```yaml
expiry: 807245762
```

### Random
If this is defined it will randomise the first objective on a scroll every 10 minutes using the list defined.  
**Example:**
```yaml
random: someListName
```

### Random Timer
Random timer allows you to override the 10 minute default timer for scroll randomisation. The value provided is in milliseconds with a minimum of 5 seconds.  
**Example:**
```yaml
randomTimer: 600000
```

### Secret
If this is defined it will allow you to have a secret code objective on your scroll as detailed bellow in trackers as well as specific rewards for guessing the secret code. A secret code does NOT require a tracker on the scroll in which case you would also want to define the next possible config which is ultrasecrets which allows for rewards to be given out when secrets are guessed correctly. To guess a secret a players uses the command `/quest secret <someSecretCode>`.  
**Example:**
```yaml
secret: someSecretCode
```

### Secret Cooldown
Secret cooldown allows you to set the cooldown between incorrect secret guesses. By default without this configuration set it is 60s.  
**Example:**
```yaml
secretCooldown: 12345
```

### Ultra Secret
This is defined as a list of commands just like rewards and the commands are run when someone correctly guesses the secret attached to the scroll.  
**Example:**
```yaml
ultraSecret:
- smite <player>
- give <player> stone 1
```

### Permission
If this is defined then when a player collects the scroll they will be given a permission for the amount of time specified in the config. This requires luckperms as it runs luckperms commands. If a player has a permission set for a specific scroll then they will be unable to collect another of the same scroll through the generator command unless the `force` modifier is used.  
The permission format is `quest.<file>_<quest>` for example `quest.coords_coords1`. If you know the name of the quest file and the quest then you can apply the permission for it to people manually to prevent them from collecting more of that quest.  
**Example:**
```yaml
permission: 40d
```

### Texture
Texture allows for a texture ID to be set on the quest so that custom textures can be used.  
**Example:**
```yaml
texture: 12345
```

### Region
Region allows you to create quests that require players to enter certain worldguard regions. When set to true you can create regions with names like `visit_the_temple_of_doom` and then have objectives such as `Visit The Temple Of Doom: Incomplete`.  
**Example:**
```yaml
region: true
```

### Movement
Movement allows for defining statistic based trackers which are mostly movement trackers but can also include things like bell ringing. When specifying values for this option, write them all on 1 line with commas seperating, no spaces.  
Available values: `cake` `shield` `climb` `sneak` `fall` `sprint` `swim` `walk` `boat` `fly` `horse` `minecart` `pig` `strider` `jump` `ring`  
Tracker format for these can be found below.  
**Example:**
```yaml
movement: cake,sprint,ring
```  
If you want to define movement based objectives dynamically using lists please scroll down to the lists documentation.
### Autocomplete
Autocomplete allows quests to be setup so that once complete they automatically get claimed. This can be useful for quest lines where people have to go and talk to a bunch of NPCs and they're split into multiple quests. This then makes it so they don't have to type `/quest claim` every time.  
This can be overridden by the global autocomplete setting in `Glboal.yml` which is defined below.
**Example:**
```yaml
autocomplete: true
```

### Coords
Coords allows you to define coordinate based objectives such as `Go To X1 Y1 Z1: Incomplete`. Coords will only be checked every 5 seconds so if using this features probably let players know in `/quest help` about that.  
**Example:**
```yaml
coords: true
```

### Effect Modifiers
Modifiers allow you to define effects players must be under to complete objectives on the scroll. For example you can add modifiers to say that a player must have slow falling and blindness to complete the objectives. To do this you do not need to set a configuration option but instead just add it as the second or third line on the scroll as seen below.  
**Example:**
```yaml
ring:
  name: 'Wildcard Quest Scroll'
  lines:
    - <player>'s Quest Scroll
    - 'Modifiers: Blindness, Slow Falling'
    - <break>
    - <empty>
    - '&fChop 10 Oak Logs: 0'
    - '&f<movement>'
    - <empty>
    - <break>
    - Use /quest help | for quest info
  rewards:
    - gquest wildcard wildcard1 <player>
```
The available effects are any a player can have. They must be the name of the effect as seen when applying it using the `/effect` command in game. For effects with underscores instead use a space as seen above with `Slow Falling`.

### Biome Modifiers
Biome modifiers allow you to define biomes players must be in to complete objectives on the scroll. For example you can add biomes to say that a player must be in the taiga or plains biome to complete the objectives. To do this you do not need to set a configuration option but instead just add it as the second or third line on the scroll as seen below.  
**Example:**
```yaml
ring:
  name: 'Wildcard Quest Scroll'
  lines:
    - <player>'s Quest Scroll
    - 'Biomes: Taiga, Plains'
    - <break>
    - <empty>
    - '&fChop 10 Oak Logs: 0'
    - '&f<movement>'
    - <empty>
    - <break>
    - Use /quest help | for quest info
  rewards:
    - gquest wildcard wildcard1 <player>
```
The available biomes are any a player can be in. They must be the name of the biome as found when using F3. For biomes with underscores instead use a space such as `Birch Forest`.

---

# Trackers
### Entities and Items
Below you will see entity type and item type mentioned. Entity type is the full entity name without the underscores. So cave_spider would be cave spider. You can find the name of an entity by looking on the wiki or doing /summon in-game and having a look through to find the actual name. It’s the same for item type so stone_bricks would be stone bricks. I would also suggest checking each tracker before releasing your scroll to ensure it works.

### Wolves
For breeding, taming and killing you can define wolves as wolves on the scroll even though their actual in game name is wolf. That way you don’t have to add “Tame 10 Wolfs: 0” and make your scroll look jank.

### Formatting
Always put a space after the : otherwise it will not work correctly. “Breed 10 Wolves: 0” and “Breed 10 Wolves:0” are not the same.
Capitalise the first letter of each word in the line to avoid other issues as I am lazy and don’t match based on just the letters. Also it keeps all the formatting the same and looking pretty.
For trackers that have complete and incomplete please capitalise the i in incomplete.

### Items and scenarios that don’t work
Most enchantments or items that cancel or modify events will cause most of the trackers to stop working properly. For example telekinesis cancels the event and then puts the item you just mined in your inventory. As a result the mining tracker will just ignore the broken block. Please alert me immediately if any of the following items or scenarios start working with the trackers. They don’t work to prevent silly interactions and quest completion abuse.

## Tracker list

### Breeding
Doesnt work with turtles coz they fuck differently apparently…  
**Example:**
```
Breed x <Entity Type>: 0  
Breed 10 Chickens: 0  
Breed 10 Wolves: 0  
```

### Crafting
**Example:**
```
Craft x <Item Type>: 0
Craft 10 Stone Bricks: 0
```

### Taking Damage
**Example:**
```
Take x Damage: 0
Take 100 Damage: 0
Take x Damage From <Entity Type>: 0
Take 100 Damage From Zombies: 0
```
`<Entity Type>` may be any of the following values:
```
A mobtype such as creeper, zombie, etc
Freeze
Fall
Drowning
Suffocation
Cacti
Falling Block (Anvil)
Wither (Potion Effect)
Hot Floor (Magma blocks)
Lightning
Fire (Standing in fire or on a campfire)
Fire Tick (Ticking out to the fire effect)
Lava
Primed TNT
Splash Potion
Area Effect Cloud (Like ender dragon fire)
Void (Dying to the void)
Starvation
Fly Into Wall
```

### Dealing Damage
**Example:**
```
Deal x Damage: 0
Deal 100 Damage: 0
Deal x Damage To <Entity Type>: 0
Deal 100 Damage To Zombies: 0
```

### Eating
**Example:**
```
Eat x <Item Type>: 0
Eat 10 Apples: 0
```

### Enchanting
**Example:**
```
Enchant x <Item Type>: 0
Enchant 10 Diamond Swords: 0
```

### Fishing
**Example:**
```
Fish x Times: 0
Catch x <Item Type>: 0
Fish 10 Times: 0
Catch 10 Pufferfish: 0
```

### Mining
Does not take into account whether or not a block was placed so try and make block breaking trackers just for blocks that change state when mined like diamond ore etc. Does not work with silk touch.
For crops it requires them to be fully grown or it will not track, that way players can't just break and place, break and place.  
**Example:**  
```
[Mine|Dig|Harvest|Chop|Break] x <Block Type>: 0
Mine 10 Redstone Ore: 0
Chop 10 Oak Logs: 0
```

### Killing Mobs
Custom mobs such as Berty need to be summoned with “CustomNameVisible:1” so that the mob is distinguishable from a mob some random player used a nametag on.  
**Example:**  
```
Kill x <Entity type>: 0
Kill 10 Chickens: 0
Kill <EntityName>: Incomplete
Kill Berty: Incomplete
```

### Biomes
**Example:**
```
Visit The <Biome Type> Biome: Incomplete
Visit The Desert Hills Biome: Incomplete
```

### Taming
**Example:**  
```
Tame x <entity type>: 0
Tame 10 Wolves: 0
```

### Throwing
**Example:**
```
Throw x <Projectile Type>: 0
Throw 10 Eggs: 0
Launch x <Projectile Type>: 0
Launch 10 Fireworks: 0
Shoot x <Projectile Type>: 0
Shoot 10 Arrows: 0
```

### Villager Trading
**Example:**
```
Trade x Times With Villagers: 0
Trade 10 Times With Villagers: 0
```

### Gaining Exp
Exp is NOT levels, it's the bit in between levels  
**Example:**
```
Gain x Exp: 0
Gain 200 Exp: 0
```

### Smelting
The thing it says to smelt should be the result of smelting, not the actual thing you smelt. So it would NOT be smelt x iron ore but instead smelt x iron ingot.
**Example:**  
```
Smelt x <smeltResult>: 0
Smelt 200 Iron Ingots: 0
```

### Secret Codes
**Example:**
```
Guess The Secret Code: Incomplete
```

### Movement
**Example:**
```
Eat x Slices Of Cake: 0
Block x Damage With A Shield: 0
Climb x Blocks: 0
Sneak x Blocks: 0
Fall x Blocks: 0
Sprint x Blocks: 0
Swim x Blocks: 0
Walk x Blocks: 0
Travel  x Blocks In A Boat: 0
Fly x Blocks: 0
Travel x Blocks On A Horse: 0
Travel  x Blocks In A Minecart: 0
Travel  x Blocks On A Pig: 0
Travel x Blocks On A Strider: 0
Jump x Times: 0
Ring x Bells: 0
```

### Shearing
**Example:**
```
Shear x <colour> Sheep: 0
Shear x White Sheep: 0
Shear x <mobType>: 0
Shear x Cows: 0
```

### Death
**Example:**  
```
Die By x: Incomplete
Die To x: Incomplete
Die From x: Incomplete
Fall Victim To x: Incomplete
x To Death: Incomplete
x And Die: Incomplete
```
X may be any of the following values:  
```
A mobtype such as creeper, zombie, etc
Freeze
Fall
Drowning
Suffocation
Cacti
Falling Block (Anvil)
Wither (Potion Effect)
Hot Floor (Magma blocks)
Lightning
Fire (Standing in fire or on a campfire)
Fire Tick (Ticking out to the fire effect)
Lava
Primed TNT
Splash Potion
Area Effect Cloud (Like ender dragon fire)
Void (Dying to the void)
Starvation
Fly Into Wall
```

### Fill Buckets
**Example:**  
```
Fill x Buckets With [Lava|Water|Milk]: 0
Catch x <mobType> In Buckets: 0
Milk x <mobType>: 0
Milk x Zombies: 0
```

### Coords
**Example:**  
```
Go To Xx Yy Zz: Incomplete
Go To X2 Y3 Z4: Incomplete
```

### Chat
**Example:**
```
Say “<someLine>”: Incomplete
Say “Welcome back Noss!”: Incomplete
```

---

# Global Configurations

The global configurations file, `Global.yml`, allows you to set certain server wide quest settings as detailed below.  
**Example global file:**
```yaml
auto: true
commands:
  weekly:
    dateFormat: ww
    permissionName: weekly
    permissionCooldown: 8d
    quest: weekly_weekly
    denyMessage: You have already collected a weekly quest scroll this week.
    description: Randomly generated weekly quest scroll.
permissions:
  set: lp user <player> permission settemp <permission> true <time>
```

## Auto
Auto allows you to override the autcomplete feature defined on scrolls at a global level. If set to false then no quest will autocomplete. If set to true then autocomplete must be defined on a per quest basis to work.

## Commands 
Commands allows you to add quests to the `/quest list` commands output as well as control how often people can collect them. All configurations are required.  
**Configuration descriptions:**  
`dateFormat` - Allows you to configure how often people can collect the scroll. Allows for `dd` `ww` `MM` `yyyy`.  
`permissionName` - A unique name for the permission used to ensure people can't collect more than 1 scroll per above specified time period. The complete permission format is `quest.<permissionName>.<dateFormat>`  
`permissionCooldown` - Time set for temporary permission. Set just longer than the time between being able to collect the scroll.  
`quest` - The location of the quest attached to this command. For example the above configuration would target `weekly.yml` and the quest called weekly within it. The first half is the file name and the second half is the quest name.  
`denyMessage` - The message displayed if the player has already collected the quest.  
`description` - The description of the quest as shown in `/quest list`.

## Permissions  
Permissions allows you to specify certain permissions commands to be used. The available placeholders are:
- `<player>` - gets replaced with the players name allowing you to specify a user.
- `<permission>` - the permission that will be set.
- `<time>` - the cooldown for the permission.

If any of the above placeholders aren't used or the permissions command supplied is incorrect in any way then the permissions features in the plugin wont work correctly.

---

# Lists
The lists file allows the creation of lists that can be used within quest configurations. The lists file enables randomisation and custom placeholders.  
**Example lists file:**
```yaml
DailyRewards:
- ie give idfk
- ie give idfk2
Daily1:
- 'Craft [5-10] Cakes: 0'
- 'Craft [15-20] Sugar: 0'
- 'Craft [10-15] Candles: 0'
- 'Craft [10-15] Tinted Glass: 0'
- 'Craft [10-20] Cookies: 0'
Daily2:
- 'Craft [10-20] <NestedList>: 0'
- 'Eat [10-20] <NestedList>: 0'
NestedList:
- Cakes
- Sugar
- etc
```
**Example quest using above lists:**
```yaml
daily:
  name: Daily Quest Scroll
  lines:
  - <player>'s Daily Quest Scroll
  - <break>
  - <empty>
  - '&f<Daily1>'
  - '&f<Daily2>'
  - <empty>
  - <break>
  - Use /quest help for quest info
  rewards:
  - give <player> some reward
```  
If you want to have movement based objectives defined in lists they need to be formatted as below. In the event that they are defined in lists then you do NOT need to define movement as a configuration option.  
**Example movement list:**
```yaml
movement:
- 'Ring 10 Bells: 0|ring'
- 'Walk 10 Blocks: 0|walk'
- 'Swim 10 Blocks: 0|swim'
```  
The configuration goes `<objective>|<movementName>` where objective is the `objective` you will see on the scroll and `movementName` is the movement config option as defined above.

---

# Quest Paths
Quest paths allow for you to specify different path options on quest so that a player can decide what they want to do. In the below example a player could choose to run `/quest path 1` which would trigger the commands listed under path 1 to be run, in this case they would be smitten. This can be used to creating branching quest lines, allow players to choose what rewards they get or even just give the illusion of choice.  
**Example Quest Path:**
```yaml
path:
  name: Quest Path Example
  lines:
    - <player>'s Quest Path
    - <break>
    - <empty>
    - '&f1. Help the bard'
    - '&f2. Help the fisherman'
    - '&f3. Help Melon'
    - <empty>
    - <break>
    - Use "/quest path number" to select a quest path
    - <empty>
    - Use /quest help for quest info
  rewards:
    - smite <player>
  path:
    '1':
      - smite <player>
    '2':
      - smite <player>
      - smite <player>
    '3':
      - gquest path path <player> force


```

---

# Folder Structure
Brief folder diagram to show where files are supposed to be located.
```
plugins
├───Quests.jar
├───Quests
│   ├───Global.yml
│   ├───Lists.yml
│   ├───questName1.yml
│   ├───questName2.yml
│   └───questNameX.yml
```

---

# Changelog
### 1.8.4
Added the ability to change the permissions command used to give players permissions to remove the requirement for luckperms.
### 1.8.2  
Added a thing to allow people to right click quests into their offhand so that bedrock players can offhand quests.
### 1.8.1  
Allowed for colour codes and `&k` almost anywhere in a quest objective line.  
Fixed problem allowing for mining stone to count as mining glowstone, sandstone, etc.  
### 1.8
Added modifiers  
Allowed for "movement" objectives to be added via lists
### 1.7.2
Added “secretCooldown” as an optional configuration option. If not set it defaults to 60s. Takes milliseconds as an input.
### 1.7.0
Added chat tracking
### 1.6.1
Added bells rung to stats trackers  
Allowed randomized quests to use nested lists
### 1.6.0
Added coords tracker
### 1.5.0
Added custom quest commands which can be defined. This allows players to collect scrolls anywhere using predefined commands with predefined cooldowns. These commands are defined as shown below in the global config file.  
Refactored generation code to allow other things to generate quests rather than just /gquest  
Added /quest list which lists all available quests to them as defined in the global config  
### 1.4.3
Added /aquest setwho <UUID> to be able to manually set UUIDs to scrolls.  
Refactored a bunch of functions to use MLib and so now quests requires MLib to be installed.  
### 1.4.1
Added <date> placeholder for quest generation and reward lines.  
Added the ability to force /aquest claim to bypass permission checks by doing /aquest claim force  
### 1.4
Added shearing and milking for all mobs while a quest has an objective for it. Shearing and milking through this method has a 30 second cooldown.
### 1.3.1
Fixed crafting objective bug
### 1.3
Commands /quest expiry and /quest secret now work with the scroll in offhand  
Added a new quest type, quest paths. This allows you to make branching questlines  
Added /quest path <num> command  
Added /aquest help and /aquest auto <true|false>  
Added autocomplete config option  
Added a global config file and first global config option  
Commands run by quests are now logged in the console  
### 1.2
Scrolls will generate into your offhand if available  
Added claimMessage config option for sending a message when the scroll is claimed  
Added noverbose option to /gquest which will cause players not to receive feedback that they have already collected a scroll when trying to collect the same one twice  
Removed deprecated add and delete reward commands  
Updated /aquest rewardlist to actually work  
### 1.1.1
Scrolls can now be claimed from your offhand
### 1.1
Added extra message options in the scroll configs allowing you to specify a name that the message is coming from and a colour for that name
### 1
Added /aquest version  
Added permission config option allowing you to prevent people from collecting the scroll if they already have the permission. The config option when set gives people the permission  
Added force option for /gquest allowing you to bypass permission config option  
Added inventory space checks for quests with ultrasecrets  
