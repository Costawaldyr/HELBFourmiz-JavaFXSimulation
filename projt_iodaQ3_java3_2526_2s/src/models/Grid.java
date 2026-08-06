package models;


import java.util.ArrayList;
import util.Coordinate;
import static util.GameConfig.*;


/**
 * Represents the game grid containing all cells of the world.
 * Responsible for managing coordinates, movement rules, and cell updates.
 */
public class Grid {
    private static final int OFFSET = 1;
    private static final int MIN_INDEX = -1;
    private static final int COLOR_PATTERN_MODULO = 2;
    private static final int EVEN_RESULT = 0;
    private static final double NO_DISTANCE = -1;


    private static final int LIGHT_GREEN_HEX = 0x59;
    private static final int DARK_GREEN_HEX = 0x95;
    private static final int DELTA = 12;

    private static final int MODE_WALKABLE = 0;
    private static final int MODE_FREE = 1;
    private static final int MODE_TARGET_OR_FREE = 2;

    private int rows;
    private int columns;

    private Cell[][] cells;

    /**
     * Creates a new game grid and initializes all its cells.
     */
    public Grid() {
        this.rows = ROWS;
        this.columns = COLUMNS;
        this.cells = new Cell[rows][columns];

        initializeGrid();
    }

    //GETTERS
    public int getRows() {return rows;}
    public int getColumns() {return columns;}
    public Cell[][] getCells() {return cells;}

    /**
     * Checks whether a coordinate is inside the boundaries of the grid.
     */
    public boolean isCoordinateInBoard(Coordinate coord) {
        return (coord.x >= INITIAL_VALEUR && coord.y >= INITIAL_VALEUR && coord.x < columns && coord.y < rows);
    }

    /**
     * Returns all adjacent coordinates (including diagonals) around a given coordinate.
     */
    public ArrayList<Coordinate> getAdjacentCoordinates(Coordinate coord) {

        ArrayList<Coordinate> resultatList = new ArrayList<>();

        for (int i = coord.x - OFFSET; i <= coord.x + OFFSET; i++) {
            for (int j = coord.y - OFFSET; j <= coord.y + OFFSET; j++) {
                if (!(i == coord.x && j == coord.y)) {
                    resultatList.add(new Coordinate(i, j));
                }
            }
        }

        return resultatList;
    }

    /**
     * Returns adjacent coordinates that are inside the grid boundaries.
     */
    public ArrayList<Coordinate> getAccessibleAdjacentCoordinates(Coordinate coord) {

        ArrayList<Coordinate> resultlList = new ArrayList<Coordinate>();
        ArrayList<Coordinate> adjacentCoordinatesList = getAdjacentCoordinates(coord);

        for (Coordinate adjacent : adjacentCoordinatesList) {
            if (isCoordinateInBoard(adjacent)) {
                resultlList.add(adjacent);
            }
        }

        return resultlList;
    }

   /**
     * Finds the best adjacent coordinate that moves closer to the target.
     *
     * @param current the current position
     * @param target the destination
     * @param gameBoard the current game
     * @param mode the movement mode used to validate coordinates
     * @return the best adjacent coordinate, or the current position if none is valid
     */
    private Coordinate findBestCoordinate(Coordinate current, Coordinate target, HelbFourmizGame gameBoard, int mode) {

        double minDistance = Double.MAX_VALUE;
        int minIndex = MIN_INDEX;

        ArrayList<Coordinate> accessibleAdjacentCoordinatesList = getAccessibleAdjacentCoordinates(current);

        for (int index = START_INDEX; index < accessibleAdjacentCoordinatesList.size(); index++) {

            Coordinate coord = accessibleAdjacentCoordinatesList.get(index);

            if (!canMoveTo(coord, target, gameBoard, mode)) { continue; }

            double distance = coord.getDistanceWith(target);

            if (distance < minDistance) {
                minDistance = distance;
                minIndex = index;
            }
        }

        if (minIndex == MIN_INDEX) {
            return current;
        }
        return accessibleAdjacentCoordinatesList.get(minIndex);
    }

    /**
     * Checks whether a coordinate can be reached according to the selected movement mode.
     *
     * @param coord the coordinate to test
     * @param target the destination
     * @param gameBoard the current game
     * @param mode the movement mode
     * @return true if the coordinate can be used
     */
    private boolean canMoveTo(Coordinate coord, Coordinate target, HelbFourmizGame gameBoard, int mode) {

        if (mode == MODE_WALKABLE) {
            return gameBoard.isWalkable(coord, target);
        }

        if (mode == MODE_TARGET_OR_FREE) {
            return coord.isSameAs(target) || gameBoard.isFreeCoordinate(coord) || gameBoard.hasStoneAt(coord);
        }

        return gameBoard.isFreeCoordinate(coord);
    }

    /**
     * Returns the next coordinate toward the target.
     * If no valid move is available, attempts to move to any free adjacent cell
     * to prevent the element from remaining blocked.
     *
     * @param currentCoordinate the current position
     * @param targetCoordinate the destination
     * @param gameBoard the current game
     * @return the next coordinate to move to
     */
    public Coordinate getNextCoordinateForTarget(Coordinate currentcCoordinate, Coordinate targetcCoordinate, HelbFourmizGame gameBoard) {

        Coordinate next = findBestCoordinate(currentcCoordinate, targetcCoordinate, gameBoard, MODE_WALKABLE);

        if (!next.isSameAs(currentcCoordinate)) {
            return next;
        }

        return findBestCoordinate(currentcCoordinate, targetcCoordinate, gameBoard, MODE_FREE);
    }

    /**
     * Returns the next coordinate toward the target.
     * The target cell itself may be occupied, making this method suitable for
     * returning to the camp or reaching another occupied destination.
     *
     * @param currentCoordinate the current position
     * @param targetCoordinate the destination
     * @param gameBoard the current game
     * @return the next coordinate to move to
     */
    public Coordinate getNextFreeCoordinateForTarget(Coordinate currentcCoordinate, Coordinate targetcCoordinate, HelbFourmizGame gameBoard) {
        return findBestCoordinate(currentcCoordinate, targetcCoordinate, gameBoard, MODE_TARGET_OR_FREE);
    }


    /**
     * Finds the nearest free coordinate to the given origin.
     * Scans the whole board and selects the free coordinate with minimal
     * Euclidean distance to the origin. Returns origin if no free coordinate
     * is found (should not happen when called after hasFreeCoordinate()).
     */
    public Coordinate findNearestFreeCoordinate(Coordinate origin, HelbFourmizGame gameBoard) {

        // Recherche d'abord une case adjacente libre
        for (Coordinate coord : getAccessibleAdjacentCoordinates(origin)) {
            if (gameBoard.isFreeCoordinate(coord)) {
                return coord;
            }
        }

      
        Coordinate best = origin;
        double bestDistance = Double.MAX_VALUE;

        for (int row = START_INDEX; row < getRows(); row++) {
            for (int col = START_INDEX; col < getColumns(); col++) {

                Coordinate coord = new Coordinate(col, row);

                if (!gameBoard.isFreeCoordinate(coord)) {
                    continue;
                }

                double distance = origin.getDistanceWith(coord);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = coord;
                }
            }
        }

        return best;
    }

    /**
     * Finds the least visited free coordinate on the board.
     * If several coordinates have the same visit count,
     * the farthest one from the origin is selected.
     *
     * @param gameBoard the current game
     * @param origin the starting coordinate
     * @return the selected destination coordinate
     */
    public Coordinate findLeastVisitedCoordinate(HelbFourmizGame gameBoard, Coordinate origin) {

        Cell[][] cells = getCells();

        Coordinate best = origin;
        int bestVisits = Integer.MAX_VALUE;
        double bestDistanceFromOrigin = NO_DISTANCE;
        boolean found = false;

        for (int row = START_INDEX; row < getRows(); row++) {

            for (int col = START_INDEX; col < getColumns(); col++) {

                Coordinate coord = new Coordinate(col, row);

                if (coord.isSameAs(origin)) {
                    continue;
                }

                if (!gameBoard.isFreeCoordinate(coord)) {
                    continue;
                }

                if (targetAlreadyChosen(gameBoard, coord)) {
                    continue;
                }

                int visits = cells[row][col].getRedColorTrace() + cells[row][col].getBlueColorTrace();
                double distanceFromOrigin = coord.getDistanceWith(origin);

                boolean isBetterVisitCount = visits < bestVisits;
                boolean isFartherOnTie = (visits == bestVisits) && (distanceFromOrigin > bestDistanceFromOrigin);

                if (!found || isBetterVisitCount || isFartherOnTie) {
                    bestVisits = visits;
                    best = coord;
                    bestDistanceFromOrigin = distanceFromOrigin;
                    found = true;
                }
            }
        }

        return best;
    }

    /**
     * Adds a red trail effect on a specific cell.
     */
    public void addRedTrails(int posX, int posY) {
        cells[posY][posX].addRedTrace(DELTA);
    }

    /**
     * Adds a blue trail effect on a specific cell.
     */
    public void addBlueTrails(int posX, int posY) {
        cells[posY][posX].addBlueTrace(DELTA);
    }

    /**
     * Resets all the red trace in the grid
     */
    public void resetRedTrails() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.resetRedTrace();
            }
        }
    }

    /**
     * Resets all the blue trace in the grid
     */
    public void resetBlueTrails() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.resetBlueTrace();
            }
        }
    }

    /**
     * Initializes the grid and assigns alternating cell colors.
     */
    private void initializeGrid() {

        for(int row = START_INDEX ; row < cells.length ; row++) {
            for(int col = START_INDEX ; col < cells[row].length ; col++) {

                int colorGreen;

                if((row + col) % COLOR_PATTERN_MODULO == EVEN_RESULT) {
                    colorGreen = LIGHT_GREEN_HEX;
                }
                else {
                    colorGreen = DARK_GREEN_HEX;
                }
                cells[row][col] = new Cell(row,col,colorGreen);
            }
        }
    }

    /**
     * Checks whether a coordinate has already been selected
     * as the destination of another seed.
     *
     * @param gameBoard the current game
     * @param coord the coordinate to test
     * @return true if the coordinate is already targeted by a seed
     */
    private boolean targetAlreadyChosen(HelbFourmizGame gameBoard, Coordinate coord) {

        for (Seed seed : gameBoard.getSeedList()) {
            if (seed.hasTarget() && seed.getTarget().isSameAs(coord)) {
                return true;
            }
        }
        return false;
    }
}
