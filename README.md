<img width="1112" height="228" alt="Title" src="https://github.com/user-attachments/assets/73879543-8f02-4c01-9839-2cfb5c227be8" />

**NoCollision** is a lightweight and configurable **Fabric server-side mod** that gives you full control over **entity collision behavior** in Minecraft.

It allows you to disable or fine-tune collision between **players**, **mobs**, and **players vs mobs**, making movement smoother and gameplay more flexible — especially on crowded servers.

---

## ✨ Features

- 👤 Disable **player ↔ player** collision  
- 🐮 Control **mob ↔ mob** collision  
- ⚔️ Customize **player ↔ mob** collision  
- 📋 Per-mob collision control using a configurable list  
- 🔄 Changes apply **instantly** (no restart required)  
- 🧩 Fully server-side (clients don’t need the mod)

---

## 🛠 Commands

All commands require **operator permission (level 2)**.

### ➕ Add a mob to the no-collision list
```bash
/nocollision add <mob_name>
````

Example:

```bash
/nocollision add minecraft:zombie
```

---

### ➖ Remove a mob from the list

```bash
/nocollision remove <mob_name>
```

---

### 📜 List all mobs in the no-collision list

```bash
/nocollision list
```

Returns all entities currently configured to ignore collision.

---

## 👥 Player ↔ Player Collision

### Enable or disable collision between players

```bash
/nocollision playersCanCollide <TRUE|FALSE>
```

* `TRUE` → Players collide normally
* `FALSE` → Players can pass through each other

> [!TIP]
> This is ideal for hubs, lobbies, minigames, and crowded areas.

---

## ⚔️ Player ↔ Hostile Mob Collision

```bash
/nocollision playersNoHostileCollision <ALL|LISTONLY|OFF>
```

| Mode       | Behavior                                                       |
| ---------- | -------------------------------------------------------------- |
| `ALL`      | Player does **not collide with any hostile mob**               |
| `LISTONLY` | Player does **not collide only with hostile mobs in the list** |
| `OFF`      | Player collides with **all hostile mobs**                      |

---

## 🐮 Player ↔ Passive Mob Collision

```bash
/nocollision playersNoPassiveCollision <ALL|LISTONLY|OFF>
```

| Mode       | Behavior                                                       |
| ---------- | -------------------------------------------------------------- |
| `ALL`      | Player does **not collide with any passive mob**               |
| `LISTONLY` | Player does **not collide only with passive mobs in the list** |
| `OFF`      | Player collides with **all passive mobs**                      |

---

## 🎯 Use Cases

* 🏟️ Crowded **lobbies and hubs**
* 🎮 **Minigames** where collision breaks movement
* 🛏️ **BedWars / SkyWars** servers
* 🧙 RPG servers with custom entity behavior
* 🐄 Farms or events where entity cramming is an issue

---

## 📦 Compatibility

* **Minecraft:** `1.21.10`
* **Mod Loader:** Fabric
* **Environment:** Server-side

> [!IMPORTANT]
> Players do **not** need to install this mod on the client.

---

## 📄 License

This project is licensed under the **MIT License**.

---

## 👤 Author

Developed by **_Nihilanth**

---

## ❤️ Contributions

Pull requests, suggestions, and issue reports are welcome.
If you find a bug or have an idea, feel free to open an issue!
