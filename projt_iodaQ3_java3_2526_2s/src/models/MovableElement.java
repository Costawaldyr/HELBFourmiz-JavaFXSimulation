package models;

import util.Coordinate;

public abstract class MovableElement extends GameElement {

    private boolean frozen;

    public MovableElement(int posX, int posY, String[] imagePaths){
        super(posX,posY, imagePaths);
        this.frozen = false;
    }

    //GETTERS AND SETTERS
    public boolean isFrozen() {return frozen; }
    public void setFrozen(boolean frozen) { this.frozen = frozen;}

    public void moveTo(Coordinate coord) {
        setPosition(coord.x, coord.y);
    }

    public abstract void move(HelbFourmizGame gameBoard);

}
