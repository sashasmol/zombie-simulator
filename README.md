# Zombie Simulator - Design Document

### UML Diagram
<img src= "Project_UML.jpg">

### Design Outline
-*ZombieSim*: This class handles the GUI and the "game loop". `KeyListener` and `MouseListener` are implemented for keyboard and mouse click responses. 

-*City*: This file functions as the main file for the simulation. It handles the array lists of zombies and humans, the boolean 2D array `walls[][]`, and calls on the methods that allow the creatures to behave appropriately.
  -`drawWalls`, `drawHumans`, and `drawZombies` are used to create the city and creatures with the appropriate restrictions such as avoding drawing creatures in walls, etc.
  -`areFacingEachOtherWithinDistance` and `isHumanInAdjacentSquareToAZombie` check if the zombie and human are facing each other and if they are adjacent, if these are both true, then the "zombification" process can occur.
  -`processZombieEatingBrain` is the process of the human becoming a zombie.
  -`processZombieSeesHuman`, `processZombieMovesTowardsHuman`, `processHumanRunAwayFromZombie`, and `processZombieSeesHuman` are functions that continuously check the parameters of the zombie and the human, calling for the infestation of the human if the cases are true.
  -`zombieDrop` drops a zombie in any random location when the mouse is clicked and `checkDropDimensions` check the location of the mouse click.

-*Creature*: This is an abstract class that is the parent class to *Human* and *Zombie* because they have some interchangable methods.
  -Both humans and zombies need x, y, direction to determine the location of the creature and the direction it is facing. 
  -`changeWhenCloseToWall` prevents creatures from running into the building walls by using the `x` and `y` coordinates of the creature and wall. If there is a wall, the direction is changed by 180 degrees.
  -`changeWhenCloseToEdge` prevents the creatures from traveling out of the screen by using the `x` and `y` coordinates, works similarly to `changeWhenCloseToWall`.
  -`move` this allows humans to run two squares when detecting a zombie instead of one.
  
-*Zombie*: This file creates the zombie's in the city as an extension of the `Creature` class and uses `changeWhenCloseToWall` and `changeWhenCloseToEdge`.
  -`processUpdateZombie` uses the Creature's `move`, but with a 20 percent chance that it changes direction.
  
-*Human*: This file creates the human's in the city as an extension of the `Creature` class and uses `changeWhenCloseToWall` and `changeWhenCloseToEdge`.
  -`turn` uses the Creature's `move`, but with a 10 percent chance that it changes direction.
  
### Special Feature
I implemented a feature that automatically "resets" the "game board" once all of the humans have been turned into zombies. 
