package models;

import util.Coordinate;

/**
 * Represents the ants' camp.
 * The camp is responsible for spawning new ants
 * on a free adjacent cell during the simulation.
 */
public class Camp extends GameElement{

    private static final String[] CAMP_IMAGE_PATH = {"/img/AntCamp.png"};
    private static final int CAMP_POS_X = 0;
    private static final int CAMP_POS_Y = 0;

    private static final int RED_PERCENT = 40;
    private static final int RANDOM_MAX = 100;

    /**
     * Creates the camp at its default position.
     */
    public Camp() {
        super(CAMP_POS_X, CAMP_POS_Y, CAMP_IMAGE_PATH);
    }



    /**
     * Executes the camp's action during the current game update.
     * The camp attempts to generate a new ant.
     *
     * @param gameBoard the current game model
     */
    @Override
    public void triggerAction(HelbFourmizGame gameBoard) {
        generateAnt(gameBoard);
    }

    /**
     * Generates a new ant on a free adjacent cell if the
     * maximum number of camp ants has not been reached.
     *
     * @param gameBoard the current game model
     */
    public void generateAnt(HelbFourmizGame gameBoard) {
        if (!gameBoard.canGenerateCampAnt()){ return; }

        for(Coordinate coord : gameBoard.getGameGrid().getAccessibleAdjacentCoordinates(getPosition())) {
            if (!gameBoard.isFreeCoordinate(coord)) { continue; }
            Ant ant = createAnt(coord);
            gameBoard.addAnt(ant);
            return;
        }
    }

    /**
     * Creates a new ant with a random colour.
     * There is a 40% chance of creating a red ant
     * and a 60% chance of creating a blue ant.
     *
     * @param coord the position where the ant will be created
     * @return the newly created ant
     */
    private Ant createAnt(Coordinate coord) {
        int random = (int)(Math.random() * RANDOM_MAX);
        if (random < RED_PERCENT){
            return new RedAnt(coord.x,coord.y);
        }
        return new BlueAnt(coord.x, coord.y);
    }
}
