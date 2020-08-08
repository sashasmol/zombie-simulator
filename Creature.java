package zombies;

import static cs2113.util.Helper.nextDouble;

public class Creature {
    public static final int facingNorth = 0;
    public static final int facingWest = 1;
    public static final int facingSouth = 2;
    public static final int facingEast = 3;

    private int currentX;
    private int currentY;
    private int direction;

    // set x
    public void setCurrentX(int x) {
        currentX = x;
    }

    // get x
    public int getCurrentX() {
        return currentX;
    }

    // set y
    public void setCurrentY(int y) {
        currentY = y;
    }

    // get y
    public int getCurrentY() {
        return currentY;
    }

    // set the direction
    public void setDirection(int d) {
        direction = d;
    }

    // get the direction
    public int getDirection() {
        return direction;
    }

    // get creatures coordinates
    public Creature(int x, int y) {
        setCurrentX(x);
        setCurrentY(y);
    }

    // turn function, calculates when creature needs to turn
    public void turn(double percentage) {
        // needs current direction and next move to work
        int dir = getDirection();
        double nextMove = nextDouble();

        // if the next move is greater than the percentage
        // the creature continues in the same direction
        if (nextMove > percentage) {
            return;
        // change to facing north
        } else if (nextMove <= percentage / 4) {
            dir = facingNorth;
        // change to facing west
        } else if (nextMove <= percentage / 2 && nextMove > percentage / 4) {
            dir = facingWest;
        // change to facing south
        } else if (nextMove <= percentage * 3 / 4 && nextMove > percentage / 2) {
            dir = facingSouth;
        // change to facing east
        } else if (nextMove <= percentage && nextMove > percentage * 3 / 4) {
            dir = facingEast;
        }
        // set current direction to new direction
        setDirection(dir);
    }

    public void changeWhenTooCloseToEdge(int width, int height) {
        int dir = getDirection();
        int x = getCurrentX();
        int y = getCurrentY();

        // turn away and change direction
        // if close to the edge on any dimension
        if (x <= 1) {
            // close to left edge
            dir = facingEast;
        } else if (x >= width - 2) {
            // close to right edge
            dir = facingWest;
        } else if (y <= 1) {
            // close to bottom of screen
            dir = facingNorth;
        } else if (y >= height - 2) {
            // close to top of screen
            dir = facingSouth;
        }
        // change current direction to changed direction
        setDirection(dir);
    }

    public void changeWhenTooCloseToWall(boolean walls[][]) {
        int dir = getDirection();
        int x = getCurrentX();
        int y = getCurrentY();

        if ((dir == facingNorth) && walls[x][y + 1]) {
            // changing to south, wall north of location
            dir = facingSouth;
        } else if ((dir == facingWest) && walls[x - 1][y]) {
            // changing to east, wall to west
            dir = facingEast;
        } else if ((dir == facingSouth) && walls[x][y - 1]) {
            // changing to north, wall is south
            dir = facingNorth;
        } else if ((dir == facingEast) && walls[x + 1][y]) {
            // changing to west, wall is east
            dir = facingWest;
        }
        // change current to new direction
        setDirection(dir);
    }

    public void move(int squaresToMove) {

        // Get current position
        // and direction of the move
        int dir = getDirection();
        int x = getCurrentX();
        int y = getCurrentY();

        switch (dir) {
            // facing north
            case facingNorth:
                y = y + squaresToMove;
                break;
            // facing west
            case facingWest:
                x = x - squaresToMove;
                break;
            //facing south
            case facingSouth:
                y = y - squaresToMove;
                break;
            // facing east
            case facingEast:
                x = x + squaresToMove;
                break;
            default:
                System.out.println("SOMETHING WENT REALLY WRONG.");
                System.exit(-1);
                break;
        }
        // change the x, y coordinate values to the new values
        setCurrentX(x);
        setCurrentY(y);
    }
}