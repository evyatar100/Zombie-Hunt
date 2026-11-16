# Zombie Hunt

A pseudo-3D zombie shooter written in Java (Swing) as a 12th-grade project.
You walk an arena, the doors open on a timer, and zombies come in from the edges.
The view is a software 3D projection: no game engine, just matrices and polygons.

The original distribution is still in this repo: `Zombie Hunt.jar` (an Eclipse
runnable jar, classes only) and the `files/` folder it loads at runtime
(sounds, fonts, the menu image, and `scores.dat`). There were no `.java`
sources in the archive. `src/` was recovered by decompiling those classes
with [CFR](https://www.benf.org/other/cfr/) 0.152. Gameplay and the look of
the code are the original ones, with a few fixes so it runs on a current JDK.

## Requirements

- JDK 17 or newer (`java` and `javac` on your `PATH`)
- A desktop session. The game opens its own window.

## Build and play

From the project root (the directory that contains `files/`):

```bash
./build.sh        # compile into build/classes
./build.sh run    # compile, then start the game
```

Or, after a build:

```bash
java -cp build/classes Demo3D
```

Start the game from this directory. Sounds, fonts, and the menu picture are
loaded from `files/` relative to the working directory.

The original jar still runs on JDK 17–21 if you prefer it:

```bash
java -jar "Zombie Hunt.jar"
```

On Linux the jar's menu picture and title font miss their files, because the
paths inside it only matched on a case-insensitive disk (`backGround.PNG`,
`door.ttf`). Use the rebuilt classes above.

## Controls

| Key / mouse        | Action                                      |
| ------------------ | ------------------------------------------- |
| Enter              | New game (from the menu)                    |
| W A S D            | Move                                        |
| Mouse              | Look                                        |
| Left click         | Shoot                                       |
| Right click        | Melee                                       |
| R                  | Reload                                      |
| Esc                | Pause, and resume from the menu             |
| F2 (on the menu)   | Cheat prompt                                |

Cheat words, typed on the menu: `reset score`, `show hits`, `add ammo`,
`one shoot`, `die`, `show map`. After `show map`, `M` during a game toggles
the grid. `add ammo` and `one shoot` turn the high score off for that run.

A round starts with the north door opening. East, south, and west follow.
Head shots score more than body shots. Ammo boxes sometimes drop from a kill.
When your health is gone the view tips over and the round ends.

## What was fixed

The recovered sources are the decompiled originals, plus only these changes:

- `Thread.stop`, `suspend`, and `resume` throw `UnsupportedOperationException`
  on current JDKs, so a new game, pause, or death could not finish. The
  animation and background-sound threads now stop and pause with a flag.
- The menu image and the title font use the real filenames
  (`files/images/backGround.png`, `files/fonts/door.TTF`).
- The mouse is recentered on the panel's real on-screen position, and the
  panel takes keyboard focus when a round starts, so looking and WASD work
  after you press New Game.
- While the zombie lists are being rebuilt, the game loop waits instead of
  spinning.
