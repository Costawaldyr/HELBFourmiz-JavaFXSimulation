package models;

import util.Coordinate;
import static util.GameConfig.*;


/**
 * Represents a movable ant in the simulation.
 * An ant searches for resources, collects them,
 * returns them to the camp, and can interact
 * with philosopher stones.
 */
public abstract class Ant extends MovableElement {

    private boolean carryingResource;
    private boolean returningToCamp;
    private boolean hasTargetResource;

    private Coordinate targetResourcePosition;


    /**
     * Creates a new ant at the specified position.
     *
     * @param posX the initial x-coordinate
     * @param posY the initial y-coordinate
     * @param imagePath the images used to display the ant
     */
    public Ant(int posX, int posY, String[] imagePath) {
        super(posX,posY,imagePath);
        this.carryingResource = false;
        this.returningToCamp = false;
        this.hasTargetResource = false;
        this.targetResourcePosition = getPosition();
    }

    //GETTERS
    public boolean isCarryingResource() {return carryingResource;}
    public boolean isReturningToCamp() {return returningToCamp;}

    //SETTERS
    public void setCarryingResource(boolean carryingResource) {this.carryingResource = carryingResource;}
    public void setReturningToCamp(boolean returningToCamp) {this.returningToCamp = returningToCamp;}

    /**
     * Executes the ant's action during the current game update.
     *
     * @param gameBoard the current game model
     */
    @Override
    public void triggerAction(HelbFourmizGame gameBoard) {
        move(gameBoard);
    }

    /**
     * Moves the ant toward its current objective.
     * The ant searches for a resource, returns to the camp
     * when carrying one, and updates its state after moving.
     *
     * @param gameBoard the current game model
     */
    @Override
    public void move(HelbFourmizGame gameBoard){

        if(isFrozen()){return;}

        Coordinate target;

        if(isReturningToCamp()){
            target = gameBoard.getCamp().getPosition();
        }
        else{
            if (hasTargetResource && !isResourceStillAvailable(gameBoard, targetResourcePosition)) {
                hasTargetResource = false;
            }

            if (!hasTargetResource) {
                if (gameBoard.getResourceList().isEmpty()) {
                    return;
                }

                Resource resource = selectTargetResource(gameBoard);

                targetResourcePosition = resource.getPosition();
                hasTargetResource = true;
            }
            target = targetResourcePosition;
        }

        Coordinate next;
        next = determineCoordinate(gameBoard, target);

        moveTo(next);
        handleArrival(gameBoard);
        dropTrace(gameBoard);
        checkStone(gameBoard);
    }

    /**
     * Forces the ant to abandon its current target
     * and immediately return to the camp.
     */
    public void returnToCamp() {
        setReturningToCamp(true);
        hasTargetResource = false;
    }

    /**
     * Forces the ant to search for a new resource.
     * Any previously selected target is discarded.
     */
    public void searchResources() {
        setReturningToCamp(false);
        hasTargetResource = false;
    }

    /**
     * Leaves a coloured trail on the current cell.
     *
     * @param gameBoard the current game model
     */
    protected abstract void dropTrace(HelbFourmizGame gameBoard);

    /**
     * Selects the resource that this ant should collect.
     * The selection strategy depends on the ant type.
     *
     * @param gameBoard the current game model
     * @return the selected resource
     */
    protected abstract Resource selectTargetResource(HelbFourmizGame gameBoard);


    /**
     * Applies the effect of a philosopher stone.
     * The ant is either transformed into a resource
     * or duplicated according to the stone probability.
     *
     * @param game the current game model
     * @param stone the collected philosopher stone
     */
    private void applyStoneEffect(HelbFourmizGame game, PhilosopherStone stone) {

        int probability = stone.getProbability();
        int random = (int)(Math.random() * probability);

        if (random == INITIAL_VALEUR) {
            transformIntoResource(game);
        } else {
            duplicate(game);
        }
    }

    /**
     * Collects the resource located at the specified position.
     * The ant starts returning to the camp after the collection.
     *
     * @param gameBoard the current game model
     * @param coord the resource position
     */
    private  void collectResource(HelbFourmizGame gameBoard, Coordinate coord) {
        for (Resource resource : gameBoard.getResourceList()) {
            if (resource.getPosition().isSameAs(coord)) {
                gameBoard.collectResource(resource);
                break;
            }
        }

        hasTargetResource = false;
        setCarryingResource(true);
        setReturningToCamp(true);
    }

    /**
     * Deposits the carried resource at the camp.
     *
     * @param gameBoard the current game model
     */
    private  void depositResource(HelbFourmizGame gameBoard) {
        gameBoard.depositResource();
        setCarryingResource(false);
        setReturningToCamp(false);
    }

    /**
     * Checks whether the ant is standing on a philosopher stone.
     * If so, the stone effect is applied.
     *
     * @param game the current game model
     */
    private void checkStone(HelbFourmizGame game) {

        for(PhilosopherStone stone : game.getPhilosopherStoneList()){

            if(getPosition().isSameAs(stone.getPosition())){
                applyStoneEffect(game, stone);
                game.collectStone(getPosition());
                return;
            }
        }
    }

    /**
     * Checks whether a resource is still present
     * at the specified position.
     *
     * @param gameBoard the current game model
     * @param position the position to check
     * @return {@code true} if the resource still exists, {@code false} otherwise
     */
    private boolean isResourceStillAvailable(HelbFourmizGame gameBoard, Coordinate position) {
        for (Resource resource : gameBoard.getResourceList()) {
            if (resource.getPosition().isSameAs(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces this ant with a resource.
     *
     * @param game the current game model
     */
    private void transformIntoResource(HelbFourmizGame game) {
        game.transformAntIntoResource(this);
    }

    /**
     * Creates a duplicate of this ant on a nearby free cell.
     *
     * @param game the current game model
     */
    private void duplicate(HelbFourmizGame game) {
        game.duplicateAnt(this);
    }

    /**
     * Handles the ant's behaviour after reaching its destination.
     * The ant either collects a resource or deposits it at the camp.
     *
     * @param gameBoard the current game model
     */
    private void handleArrival(HelbFourmizGame gameBoard) {

        if (isReturningToCamp()) {

            if (getPosition().isSameAs(gameBoard.getCamp().getPosition())) {
                depositResource(gameBoard);
            }
            return;
        }

        if (getPosition().isSameAs(targetResourcePosition)) {
            collectResource(gameBoard, targetResourcePosition);
        }
    }

    /**
     * Determines the next coordinate the ant should move to.
     * When returning to the camp, the ant only moves through free cells
     * (except for the camp itself). Otherwise, it follows the normal
     * movement rules to reach its current target.
     *
     * @param gameBoard the current game model
     * @param target the destination the ant is moving toward
     * @return the next coordinate where the ant should move
     */
    private Coordinate determineCoordinate(HelbFourmizGame gameBoard, Coordinate target) {

        if (isReturningToCamp()) {
           return gameBoard.getGameGrid().getNextFreeCoordinateForTarget(getPosition(), target, gameBoard);
        } else {
            return gameBoard.getGameGrid().getNextCoordinateForTarget(getPosition(), target, gameBoard);
        }
    }
}
