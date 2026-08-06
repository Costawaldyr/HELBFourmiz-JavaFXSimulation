package models;

import util.GameConfig;



/**
 * Represents a red ant.
 * Red ants leave red pheromone trails and always target
 * the closest available resource.
 */
public class RedAnt extends Ant{

    private static final String[] IMAGE_PATHS = {"/img/red_ant.png"};


    /**
     * Creates a new red ant at the specified position.
     *
     * @param posX the x-coordinate
     * @param posY the y-coordinate
     */
    public RedAnt(int posX,int posY) {
        super(posX,posY,IMAGE_PATHS);
    }

    /**
     * Leaves a red pheromone trail on the ant's current cell.
     *
     * @param gameBoard the current game model
     */
    @Override
    protected void dropTrace(HelbFourmizGame gameBoard) {
        gameBoard.getGameGrid().addRedTrails(getPosX(), getPosY());
    }


    /**
     * Selects the closest available resource.
     *
     * @param gameBoard the current game model
     * @return the nearest resource
     */
    @Override
    public Resource selectTargetResource(HelbFourmizGame gameBoard) {

        int bestIndex = GameConfig.START_INDEX;
        double bestDistance = Double.MAX_VALUE;
        boolean found = false;

        for (int i = GameConfig.START_INDEX; i < gameBoard.getResourceList().size(); i++) {
            Resource resource = gameBoard.getResourceList().get(i);
            double distance = getPosition().getDistanceWith(resource.getPosition());

            if (!found || distance < bestDistance) {
                found = true;
                bestDistance = distance;
                bestIndex = i;
            }
        }

        return gameBoard.getResourceList().get(bestIndex);
    }
}
