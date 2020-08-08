# Zombie Simulator - Design Document

### UML Diagram
<img src= "Project_UML.jpg">

### Design Outline
The turn method takes in a percentage, zombies have a 20% chance of turning and humans have a 10% chance of turning. I generalize the parameter as a percentage because the creature class is used for both zombies and humans. The function turn uses the previous function to get the current direction of the creature and calculates the next move by using a random generator from the code I am given. I use the next move to predict the creatures next step. If the next move is a value less than or equal to the percentage divided by 4, then the creature’s direction is turned to face north. If the next move value is less than or equal to the percentage divided by 2 AND greater than the percentage divided by 4, then the creature is turned to face west. If the next move value is less than or equal to the percentage multiplied by 3⁄4 AND greater than the percentage divided by 2, then the creature is turned to face south. Finally, if the next move value is less than or equal to the percentage AND greater than the percentage multiplied by 3⁄4, then the creature is turned to face west.
