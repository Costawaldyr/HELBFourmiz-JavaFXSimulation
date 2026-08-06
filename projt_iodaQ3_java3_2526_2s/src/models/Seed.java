package models;

import util.Coordinate;


/**
 * Represents a flying seed generated when a resource is collected.
 * A seed moves toward a target location and becomes a new resource
 * once it reaches its destination.
 */
public class Seed extends MovableElement {

    private static final String[] IMAGE_PATHS = {"/img/seed.png" };

    private boolean hasTarget;
    private Coordinate target;

    /**
     * Creates a new seed at the specified position.
     *
     * @param posX the initial x-coordinate
     * @param posY the initial y-coordinate
     */
    public Seed(int posX, int posY) {
        super(posX, posY, IMAGE_PATHS);
        this.hasTarget = false;
        this.target = getPosition();
    }

    //GETTERS
    public Coordinate getTarget() {return target;}
    public boolean hasTarget() { return hasTarget;}
    public boolean hasArrived(){ return getPosition().isSameAs(target);}

    //setter
    public void setTarget(Coordinate target) {this.target = target; hasTarget = true;}


    /**
     * Executes the seed's action during the current game update.
     * The seed moves toward its target and germinates when it arrives.
     *
     * @param gameBoard the current game model
     */
    @Override
    public void triggerAction(HelbFourmizGame gameBoard) {

        move(gameBoard);

        if (hasArrived()) {
            gameBoard.germinateSeed(this);
            return;
        }

    }

    /**
     * Moves the seed one step closer to its target.
     * The seed does not move if it is frozen or has no destination.
     * Any philosopher stone encountered on its path is collected.
     *
     * @param gameBoard the current game model
     */
    @Override
    public void move(HelbFourmizGame gameBoard){

        if (isFrozen() || !hasTarget) {
            return;
        }

        Coordinate nextPosition = gameBoard.getGameGrid().getNextFreeCoordinateForTarget(getPosition(), target, gameBoard);
        moveTo(nextPosition);
        gameBoard.collectStone(getPosition());
    }

}
