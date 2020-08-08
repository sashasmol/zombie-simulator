package cs2113.zombies;

import zombies.Creature;

public class Zombie extends Creature {

    public Zombie(int x, int y) {
        super(x, y);
    }

    public void processUpdateZombie(boolean[][] walls, int width, int height) {
        // 20% change of turning
        turn(0.2);
        changeWhenTooCloseToEdge(width, height);
        changeWhenTooCloseToWall(walls);
        move(1);
    }
}