# Quentin project for Software Development Methods exam 🧩♟️

_Gianluca Guglielmo_

Quentin is a game designed by Luis Bolaños Mures in 2012. It is worth noting that it has recently been renamed _Quinten_, along with a slight update to its original rules.
To avoid any version conflict, this game implementation will explicitly refer to the original 2012 rules, which are here described in the [rules](#Rules) section.

## Rules

### Introduction
Quentin is a connection game for two players: Black and White. It's played on the intersections (points) of a square board, which is initially empty. The top and bottom
edges of the board are colored black; the left and right edges are colored white.

The size of the board is not fixed.

### Definitions
Stones are placed on the intersections of the board. They could form the following objects, which are noteworthy for the description of the game:
- chain: set of like-colored, orthogonally adjacent stones;
- region: a set of orthogonally adjacent empty points completely surrounded by stones or board edges;
- territory: if all points in a region are orthogonally adjacent to at least two stones, said region is also a territory.

### Play
Black plays first, then turns alternate. 

On his turn, a player must place one stone of his color on an empty point. 

Then, every territory on the board is filled with stones of the player who has the majority of stones orthogonally adjacent to it, after which the turn ends. Territories with the same number of Black and
White stones adjacent to it are filled with stones of the opponent's color. 

At the end of a turn, any two like-colored, diagonally adjacent stones must share at least one orthogonally adjacent, like-colored neighbor. Otherwise, the move is illegal and the player must choose another one.
If a player can't make a move on his turn, he must pass. Passing is otherwise not allowed. 

There will always be a move available to at least one of the players, unless intersections have been filled entirely. 

The game is won by the player who completes a chain of his color touching the two opposite board edges of his color.

A draw is possible if the board is filled entirely without any winner, or if both players win at the same time, which is a rare event made possible by the territory filling rule.

### Pie rule
The pie rule is used in order to make the game fair. This means that White will have the option, on his first turn only, to change sides instead of making a regular move.

## Execution instructions
The game can be compiled, tested and executed using the standard Gradle instructions:
- to compile an executable jar: `gradle jar`;
- to run the application: `gradle run`;
- to run the tests: `gradle test`.