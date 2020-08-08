package cs2113.zombies;

import cs2113.util.Helper;
import java.awt.*;
import java.util.ArrayList;

public class City {

    /** Walls is a 2D array with an entry for each space in the city.
     * If walls[x][y] is true, that means there is a wall in the space.
     * else the space is free. Humans should never go into spaces that
     * have a wall. */

    private boolean walls[][];
    private int width, height;
    ArrayList<Human> humans;
    ArrayList<Zombie> zombies;

    /** Create a new City and fill it with buildings and people.
     * @param w    width of city
     * @param h    height of city
     * @param numB number of buildings
     * @param numP number of people */

    public City(int w, int h, int numB, int numP) {
        width = w;
        height = h;
        walls = new boolean[w][h];
        humans = new ArrayList<Human>();
        zombies = new ArrayList<Zombie>();

        // number of buildings
        randomBuildings(numB);
        // number of people
        populate(numP);
        //number of zombies
        infestation(1);
    }

    private void infestation(int numZombies) {
        int tx, ty;
        for (int i = 0; i < numZombies; i++) {
            tx = Helper.nextInt(width);
            ty = Helper.nextInt(height);
            while (isDisallowedForCreatureSpot(tx, ty)) {
                tx = Helper.nextInt(width);
                ty = Helper.nextInt(height);
            }
            Zombie zombie = new Zombie(tx, ty);
            zombies.add(zombie);
        }
    }


    /** Generates numPeople random people distributed throughout the city.
     * People must not be placed inside walls!
     * @param numPeople the number of people to generate */

    private void populate(int numPeople) {
        int tx, ty;
        // for loop used to create humans
        for (int i = 0; i < numPeople; i++) {
            tx = Helper.nextInt(width);
            ty = Helper.nextInt(height);
            // checking for out of bounds
            while (isDisallowedForCreatureSpot(tx, ty)) {
                tx = Helper.nextInt(width);
                ty = Helper.nextInt(height);
            }
            // set temp x,y coordinate to new human coordinate
            Human human = new Human(tx, ty);
            // add the human to the city
            humans.add(human);
        }
    }

    public boolean isDisallowedForCreatureSpot(int tx, int ty) {
        boolean result = false;
        // check for location to be on screen
        if (!(tx < 0 || tx >= width || ty < 0 || ty >= height)) {
            result = walls[tx][ty];
        }
        return result;
    }

    /** Generates a random set of numB buildings.
     * @param numB the number of buildings to generate! */

    private void randomBuildings(int numB) {
        /* Create buildings of a reasonable size for this map */
        int bldgMaxSize = width / 6;
        int bldgMinSize = width / 50;

        /* Produce a bunch of random rectangles and fill in the walls array */
        for (int i = 0; i < numB; i++) {
            int tx, ty, tw, th;
            tx = Helper.nextInt(width);
            ty = Helper.nextInt(height);
            tw = Helper.nextInt(bldgMaxSize) + bldgMinSize;
            th = Helper.nextInt(bldgMaxSize) + bldgMinSize;

            for (int r = ty; r < ty + th; r++) {
                if (r >= height)
                    continue;
                for (int c = tx; c < tx + tw; c++) {
                    if (c >= width)
                        break;
                    walls[c][r] = true;
                }
            }
        }
    }

    /** Draw the buildings.
     * First set the color for drawing, then draw a dot at each space
     * where there is a wall. */

    private void drawWalls() {
        ZombieSim.dp.setPenColor(Color.darkGray);
        // drawing buildings, created dot by dot
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (walls[c][r]) {
                    ZombieSim.dp.drawDot(c, r);
                }
            }
        }
    }

    private void drawHumans() {
        for (int i = 0; i < humans.size(); i++) {
            ZombieSim.dp.setPenColor(Color.white);
            ZombieSim.dp.drawDot(humans.get(i).getCurrentX(), humans.get(i).getCurrentY());
        }
    }

    private void drawZombies() {
        for (int i = 0; i < zombies.size(); i++) {
            ZombieSim.dp.setPenColor(Color.yellow);
            ZombieSim.dp.drawDot(zombies.get(i).getCurrentX(), zombies.get(i).getCurrentY());
        }
    }

    /** Updates the state of the city for a time step. */

    public void update() {
        processZombieSeesHuman();
        processZombieEatingBrain();
        //Set base parameters for human processing
        // Move humans, zombies, etc
        for (int i = 0; i < humans.size(); i++) {
            humans.get(i).processUpdateHuman(walls, width, height);
        }
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).processUpdateZombie(walls, width, height);
        }
    }

    private void processZombieSeesHuman() {
        int squaresToMove = 2;
        int distanceBetween = 10;
        for (int j = 0; j < zombies.size(); j++) {
            for (int i = 0; i < humans.size(); i++) {
                Human h = humans.get(i);
                Zombie z = zombies.get(j);
                // if they are facing eachother within 10 blocks, TRUE
                boolean isItTrue = areFacingEachOtherWithinDistance(h, z, distanceBetween);
                if (isItTrue) {
                    // human runs 2 blocks away
                    processHumanRunAwayFromZombie(h, squaresToMove);
                    // zombie moves towards human
                    processZombieMovesTowardsHuman(z, 1);
                    return;
                }
            }
        }
    }

    private void processZombieMovesTowardsHuman(Zombie z, int squaresToMove) {
        int zDir = z.getDirection();
        int zombieX = z.getCurrentX();
        int zombieY = z.getCurrentY();

        // facing north
        if (zDir == z.facingNorth) {
            // human is north, move north
            zombieY = zombieY + squaresToMove;
        // facing west
        } else if (zDir == z.facingWest) {
            // human is west, move west
            zombieX = zombieX - squaresToMove;
        //facing south
        } else if (zDir == z.facingSouth) {
            // human is south, move south
            zombieY = zombieY - squaresToMove;
        // facing east
        } else if (zDir == z.facingEast) {
            // human is east, move east
            zombieX = zombieX + squaresToMove;
        }
    }

    private void processHumanRunAwayFromZombie(Human h, int squaresToMove) {
        int hDir = h.getDirection();
        int suggestedHumanX = h.getCurrentX();
        int suggestedHumanY = h.getCurrentY();

        // inverse of the ZombieMovesTowardsHuman function
        // facing north
        if (hDir == h.facingNorth)  {
            suggestedHumanY = suggestedHumanY - squaresToMove;
        // facing west
        } else if (hDir == h.facingWest) {
            suggestedHumanX = suggestedHumanX - squaresToMove;
        //facing south
        } else if (hDir == h.facingSouth) {
            suggestedHumanY = suggestedHumanY + squaresToMove;
        // facing east
        } else if (hDir == h.facingEast) {
            suggestedHumanX = suggestedHumanX + squaresToMove;
        }

        // do not need this, but doesn't hurt
        if (!isDisallowedForCreatureSpot(suggestedHumanX, suggestedHumanY)) {
            h.setCurrentX(suggestedHumanX);
            h.setCurrentY(suggestedHumanY);
        }
    }

    private boolean areFacingEachOtherWithinDistance(Human h, Zombie z, int distanceBetween) {
        int hDir = h.getDirection();
        int zDir = z.getDirection();
        int hx = h.getCurrentX();
        int hy = h.getCurrentY();
        int zx = z.getCurrentX();
        int zy = z.getCurrentY();

        boolean facingEachOther = false;

        // checking y-coordinates for north and south
        facingEachOther = (zDir == z.facingNorth && hDir == h.facingSouth) && (zy - hy > 0 && zy - hy < distanceBetween && zx == hx);
        facingEachOther = facingEachOther || (zDir == z.facingSouth && hDir == h.facingNorth) && (hy - zy > 0 && hy - zy < distanceBetween && zx == hx);
        // checking x-coordinates for east and west
        facingEachOther = facingEachOther || (zDir == z.facingEast && hDir == h.facingWest) && (zx - hx > 0 && zx - hx < distanceBetween && hy == zy);
        facingEachOther = facingEachOther || (zDir == z.facingWest && hDir == h.facingEast) && (hx - zx > 0 && hx - zx < distanceBetween && hy == zy);

        return facingEachOther;
    }

    private void processZombieEatingBrain() {
        for (int j = 0; j < zombies.size(); j++) {
            for (int i = 0; i < humans.size(); i++) {
                Human h = humans.get(i);
                Zombie z = zombies.get(j);
                // checks if the human and zombie are adjacent
                // if they are, then the function is true
                boolean isItTrue = isAHumanInAdjacentSquareToAZombie(h, z);
                if (isItTrue) {
                    // Zombie is eating Human and changes places
                    int x = h.getCurrentX();
                    int y = h.getCurrentY();
                    Zombie newZ = new Zombie(x, y);
                    // add a zombie in location of human
                    zombies.add(newZ);
                    // remove current human
                    humans.remove(i);
                    return;
                }
            }
        }
    }

    private boolean isAHumanInAdjacentSquareToAZombie(cs2113.zombies.Human human, Zombie zombie) {
        int zx = zombie.getCurrentX();
        int zy = zombie.getCurrentY();
        int hx = human.getCurrentX();
        int hy = human.getCurrentY();

        // checking if x-values are adjacent
        boolean thisIsIt = (zx == hx + 1 && zy == hy);
        thisIsIt = thisIsIt || (zx == hx - 1 && zy == hy);
        // checking if y-values are adjacent
        thisIsIt = thisIsIt || (zx == hx && zy == hy - 1);
        thisIsIt = thisIsIt || (zx == hx && zy == hy + 1);

        // if one of four is true, then they are adjacent
        if (thisIsIt) {
            return true;
        } else {
            return false;
        }
    }

    public void zombieDrop(int x, int y) {
        if(!isDisallowedForCreatureSpot(x,y)) {
            zombies.add(new Zombie(x, y));
        }
    }

    public boolean checkDropDimensions(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        } else if (walls[x][y]) {
            return false;
        } else {
            return true;
        }
    }

    /** Draw the buildings and all humans. */

    public void draw() {
        // Clear the screen
        cs2113.zombies.ZombieSim.dp.clear(Color.black);
        drawWalls();
        drawHumans();
        drawZombies();
    }
}