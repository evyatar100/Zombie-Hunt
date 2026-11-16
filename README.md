# Zombie Hunt

![Three blocky zombies: small, huge, and normal](docs/zombies.jpg)

A pseudo-3D shooter from 12th grade. Night, a fenced arena, doors that open on a timer, and green zombies with red hands. The picture is drawn with matrices and polygons.

![The graveyard menu](docs/menu.png)

![The open door, the rifle, and a zombie in the yard](docs/arena.png)

![A hit: the score jumps to 125](docs/hit.png)

JDK 17 or newer, from this folder:

```bash
./build.sh run
```

Or with Docker (browser via noVNC):

```bash
docker build -t zombie-hunt .
docker run --rm -p 6080:6080 zombie-hunt
```

Then open http://localhost:6080/vnc.html

Enter starts a round. WASD moves, the mouse looks, left click shoots, right click is a punch, R reloads, Esc pauses.
