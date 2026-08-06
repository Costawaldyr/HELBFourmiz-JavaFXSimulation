package models;

import util.Coordinate;


/**
 *  Represents any element that can exist on the game
 *  this class centralizes the position  X, Y and the image path for all subClasses
 */
public abstract class GameElement {

    private static final int FIRST_INDEX = 0;
    private int posX;
    private int posY;
    private final String[] IMAGE_PATHS;

    public GameElement(int posX, int posY, String[] imagePaths) {
        this.posX = posX;
        this.posY = posY;
        this.IMAGE_PATHS = imagePaths;
    }

    //GETTERS
    public int getPosX(){return posX;}
    public int getPosY(){return posY;}
    public Coordinate getPosition() {return new Coordinate(posX, posY);}

    public String getPathToImage(){return IMAGE_PATHS[FIRST_INDEX];}
    public String getPathToImage(int index){return IMAGE_PATHS[index];}
    public int getPathToImageLen(){return IMAGE_PATHS.length;}

    //SETTERS
    public void setPosX(int newPosX){posX = newPosX;}
    public void setPosY(int newPosY){posY = newPosY;}
    public void setPosition(int newPosX, int newPosY) {this.posX = newPosX; this.posY = newPosY;}

    //ABSTRACT METHODE
    public abstract void triggerAction(HelbFourmizGame gameBoard);
}
