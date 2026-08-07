# HELBFourmiz — Real-Time Ant Colony Simulation

HELBFourmiz is a real-time ant colony simulation developed for the **Java III** course at HELB. It renders a colony of ants collecting resources on a grid, built entirely with core Java and JavaFX under strict academic constraints — no external libraries, no Java Streams, no `Optional`, no generics, and a single animation thread.

## 🐜 Overview

A colony (camp) spawns ants one by one. Each ant type has its own behavior:
- **Red ants** move toward the *nearest* available resource.
- **Blue ants** move toward the *farthest* available resource.

Both types collect resources and return to the camp, leaving a visible colored trail behind them as they move.

## 🚀 Features

- **Dual ant behavior** — red (nearest-resource) and blue (farthest-resource) targeting strategies, both with custom pathfinding.
- **Resource regeneration** — collecting a resource spawns a *seed* that migrates to the least-visited free cell on the grid and germinates into a new resource.
- **Philosopher's Stone power-up** — three tiers (yellow / orange / red) with probabilistic effects: a collecting ant may turn into a resource or duplicate itself onto the nearest free cell.
- **Dynamic visual trails** — each tile's color shifts as ants pass over it, making colony activity visible at a glance.
- **Configurable simulation speed** — default refresh rate of 0.5s per action, adjustable (e.g. down to 0.1s).
- **Debug / cheat keyboard shortcuts** — spawn entities, freeze specific groups, force actions, and end the simulation on demand (see table below).
- **Save system** — ending the simulation writes a `save.jds` file with the final ant and resource counts.

### Keyboard shortcuts (debug mode)

| Key | Action |
|---|---|
| `a` | Spawn a red ant |
| `z` | Spawn a blue ant |
| `e` | Spawn a resource |
| `r` | Spawn a seed |
| `w` / `x` / `c` / `v` | Freeze red ants / blue ants / seeds / everything |
| `j` | Force-collect all resources |
| `k` | Force all ants back home |
| `l` | Force all ants toward a resource |
| `m` | End simulation and write `save.jds` |
| `u` | Spawn an extra Philosopher's Stone |
| `i` / `o` / `p` | Reset red / blue / all ant trails |

## 🧰 Tech Stack

| Technology | Role in the project |
|---|---|
| **Java** | Core language — plain OOP, no streams or generics, per the course's academic constraints. |
| **JavaFX (Canvas + Timeline)** | Renders the grid and drives the simulation loop. A single `Timeline` acts as the entire game loop — no additional threads are used. |
| **Custom pathfinding** | Hand-written nearest/farthest-resource targeting logic (no pathfinding library). |

## 🏗 Architecture

Strict object-oriented hierarchy, following an MVC separation imposed by the course:

```
GameElement (abstract)
   └── MovableElement (abstract)
          ├── Ant (Red / Blue behavior)
          └── Seed
```

```
src/
├── models/       # GameElement hierarchy, Ant, Seed, Grid, Resource, Philosopher's Stone
├── controller/   # Simulation logic, input handling, cheat codes
├── view/         # JavaFX rendering (Canvas, tile drawing, colony visualization)
├── util/         # Constants, helpers
└── game/         # Timeline / main simulation loop
```

## 📐 Academic Constraints

This project was built under the following mandatory restrictions:
- A single `Timeline` — no additional threads.
- No Java Streams, no `Optional`, no generics/custom templates.
- No design patterns beyond what naturally emerges from the required class hierarchy.
- All identifiers (classes, methods, variables) in English; comments in French, as required by the course.
- No `null` in working logic; no magic constants (all thresholds/config are named constants).

## 📋 Prerequisites

- OpenJDK (version matching the course's reference environment)
- A Linux (Ubuntu) environment is recommended — the project targets the course's VM (`JUbuntu2526_AF`)

## 🚦 How to Run

The project includes a `run.sh` script that compiles and launches the application in a single command:

```bash
bash run.sh
```

## 👤 Author

**Waldyr Costa** — Application Development student at HELB
[GitHub](https://github.com/Costawaldyr) · [LinkedIn](https://www.linkedin.com/in/waldyr-c-b38304257/)
