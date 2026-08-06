package models;

/**
 * Represents a single cell of the game grid.
 * each cell stores it's position and the traces left by red or blue ants.
 */
public class Cell {
    private static final int MAX_COLOR  = 255;
    private static final int DEFAULT_COLOR_TRACE = 0;

    private int row;
    private int column;
    private int green;

    private int redColorTrace;
    private int blueColorTrace;

    public Cell(int row, int column, int green) {
        this.row = row;
        this.column = column;
        this.green = green;
        this.redColorTrace = DEFAULT_COLOR_TRACE;
        this.blueColorTrace = DEFAULT_COLOR_TRACE;
    }

    //GETTERS
    public int getGreenColor(){ return green; }
    public int getRedColorTrace(){ return redColorTrace; }
    public int getBlueColorTrace(){ return blueColorTrace; }

    //Functions

    /**
     * Increases the red trace left by a red ant.
     * @param delta amount added to the current red trace
     */
    public void addRedTrace(int delta) {
        redColorTrace = increaseTrace(redColorTrace, delta);
    }

    /**
     * Increases the blue trace left by a blue ant.
     * @param delta amount added to the current blue trace
     */
    public void addBlueTrace(int delta) {
        blueColorTrace = increaseTrace(blueColorTrace, delta);
    }

    /**
     * Resets the red and blue  trace to its default value.
     */
    public void resetRedTrace() {
        redColorTrace = resetTrace();
    }

    public void resetBlueTrace() {
        blueColorTrace = resetTrace();
    }


    /**
     * Increases a trace value without exceeding the maximum color value.
     *
     * @param currentValue current trace value
     * @param delta value to add
     * @return the updated trace value
     */
    private int increaseTrace(int currentValue, int delta) {
        currentValue += delta;

        if (currentValue > MAX_COLOR) {
            currentValue = MAX_COLOR;
        }
        return currentValue;
    }

    private int resetTrace() {
        return DEFAULT_COLOR_TRACE;
    }
}
