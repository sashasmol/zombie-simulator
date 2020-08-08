package cs2113.zombies;

import zombies.Creature;

public class Human extends Creature {

    public Human(int x, int y) {
        super(x, y);
    }

    public void processUpdateHuman(boolean[][] walls, int width, int height) {
        // 10% chance of turning
        turn(0.1);
        changeWhenTooCloseToEdge(width, height);
        changeWhenTooCloseToWall(walls);
        // move one unless zombie is near, this is changed in City
        move(1);
    }
}