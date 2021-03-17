# MinPath

> MinPath is an app that demonstrates use of [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) to find shortest path in a 2d grid.

---

### Table of Contents
- [Description](#description)
- [How To Use](#how-to-use)
- [Author Info](#author-info)

---

## Description

The app uses the MVVM model of code structuring. Components and their are as follows:
- Model - Contains model of the cell in grid.
- MinPathViewModel - Contains and manipulates the LiveData objects.
- GridService - Contains the business logic to find the min path.
- MinPathActivity - Holds the view.


#### Technologies

- Android
- Kotlin

[Back To The Top](#read-me-template)

---

## How To Use
Clone and build the source code on android studio or download the apk.
Once installed:
- Click on any cell to create a path blocker
![ss1](https://github.com/Arvindmehra07/android-minpath/blob/master/screenshots/ss_obstacle_only.png?raw=true)

- Click on 'Find Path' to see the shortest path (if possible).
![ss2](https://github.com/Arvindmehra07/android-minpath/blob/master/screenshots/ss_fiiled_path.png?raw=true)
- Click replay(at top right) to reset the board.
- Click help(at top left) for more instructions.

---

## Author Info

- Twitter - [@_Arvind_Mehra](https://twitter.com/_Arvind_Mehra)
- Linkdin - [Arvind Mehra](https://www.linkedin.com/in/arvind-mehra-9b2280141/)

[Back To The Top](#read-me-template)