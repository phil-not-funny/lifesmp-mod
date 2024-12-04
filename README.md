# Life-SMP Mod  
### Inspired by [Grian's Life Series](https://www.youtube.com/c/grian)  

This mod brings the unique gameplay mechanics of [Grian's popular "Life SMP"](https://www.youtube.com/playlist?list=PLU2851hDb3SHLdAlj8dxqHPeT_qIBbRBv) to you and your friends.  

---

## Features  
Life-SMP Mod is designed to replicate the gameplay from the Life Series. Whether you're giving lives, managing the Boogeyman mechanic, or customizing game rules, this mod ensures every session to be the way you wanted the Life-Series to turn out.

---

## Installation  

To download the mod, head over to the **[Releases tab](https://github.com/phil-not-funny/lifesmp-mod/releases)** on GitHub. Ensure your setup matches these requirements:  
- **Minecraft version:** 1.21.3  
- **Fabric Loader version:** 0.16.9-1.21.3  
- **Fabric API version:** 0.107.0+1.21.3  

---

## Commands  
Here are the commands available to enhance your Life-SMP experience:  

### Player Commands  
- `/give_life <player>`  
  Transfer one of your lives to another player.  

### Operator Commands  
- `/set_lives <player> <amount>`  
  Set the exact number of lives for any player.  
- `/boogeyman start`  
  Start the Boogeyman countdown.  
- `/boogeyman end`  
  End the Boogeyman phase. Remaining Boogeymen will drop to 1 life.  
- `/boogeyman cure <player>`  
  Manually cure a Boogeyman.  

---

## Configuration  

Customize your Life-SMP gameplay using these gamerules:  

- **`lifesmp_startLives <number>`**  
  Set the starting number of lives for players. (Default: 3)  
- **`lifesmp_boogeyTimer <number>`**  
  Define the time in minutes before a Boogeyman is selected after the first warning. (Default: 5)  
- **`lifesmp_boogeySecondProbability <number>`**  
  Adjust the probability multiplier for additional Boogeymen being chosen. (Default: 0.2)  
- **`lifesmp_allowGiveLifeToGreys <true/false>`**  
  Whether to allow players to "revive" players with 0 lives by giving them theirs (through /givelife). (Default: false)  
- **`lifesmp_allowHelmets <true/false>`**  
  Whether to allow players to use helmets or not (Default: false)  
