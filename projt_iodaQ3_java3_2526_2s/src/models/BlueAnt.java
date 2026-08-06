package models;

import util.GameConfig;


/**
 * Represents a blue ant.
 * Blue ants leave blue pheromone trails and always target
 * the farthest available resource.
 */
public class BlueAnt extends Ant {

    private static final String[] IMAGE_PATHS = {"/img/blue_ant.png"};
    private static final double NO_DISTANCE = -1;


    /**
     * Creates a new blue ant at the specified position.
     *
     * @param posX the x-coordinate
     * @param posY the y-coordinate
     */
    public BlueAnt(int posX, int posY){
        super(posX,posY,IMAGE_PATHS);
    }

     /**
     * Leaves a blue pheromone trail on the ant's current cell.
     *
     * @param gameBoard the current game model
     */
    @Override
    protected void dropTrace(HelbFourmizGame gameBoard) {
        gameBoard.getGameGrid().addBlueTrails(getPosX(), getPosY());
    }

     /**
     * Selects the farthest available resource.
     *
     * @param gameBoard the current game model
     * @return the farthest resource
     */
    @Override
    public Resource selectTargetResource(HelbFourmizGame gameBoard) {

        int bestIndex = GameConfig.START_INDEX;
        double bestDistance = NO_DISTANCE;
        boolean found = false;

        for (int i = GameConfig.START_INDEX; i < gameBoard.getResourceList().size(); i++) {
            Resource resource = gameBoard.getResourceList().get(i);
            double distance = getPosition().getDistanceWith(resource.getPosition());

            if (!found || distance > bestDistance) {
                found = true;
                bestDistance = distance;
                bestIndex = i;
            }
        }

        return gameBoard.getResourceList().get(bestIndex);
    }
}
