package util;

/**
 * Represents a position on the game grid using x and y coordinates.
 * This class also provides utility methods to compare coordinates
 * and calculate the distance between two positions.
 */
public class Coordinate {

    public int x = 0; 
    public int y = 0; 

    /**
     * Creates a coordinate with the specified x and y values.
     *
     * @param x the horizontal position
     * @param y the vertical position
     */
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the Euclidean distance between this coordinate
     * and another coordinate.
     *
     * @param coord the coordinate to measure the distance to
     * @return the distance between the two coordinates
     */
    public double getDistanceWith(Coordinate coord){
        return Math.sqrt((coord.y - this.y) * (coord.y - this.y) + (coord.x - this.x) * (coord.x - this.x));
    }

    /**
     * Checks whether this coordinate has the same position as another coordinate.
     *
     * @param coord the coordinate to compare with
     * @return {@code true} if both coordinates have the same x and y values, {@code false} otherwise
     */
    public boolean isSameAs(Coordinate coord) {
        return (this.x == coord.x && this.y == coord.y);
    }
}
