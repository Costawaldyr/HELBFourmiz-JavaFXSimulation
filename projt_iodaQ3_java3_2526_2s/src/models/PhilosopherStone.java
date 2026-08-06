package models;

import models.enums.StoneType;

import java.util.Random;

/**
 * Represents a philosopher stone.
 * Each stone has a random type that determines its appearance
 * and the probability of its effect when collected.
 */
public class PhilosopherStone extends GameElement {

    private static final int YELLOW_IMAGE_INDEX = 0;
    private static final int ORANGE_IMAGE_INDEX = 1;
    private static final int RED_IMAGE_INDEX = 2;

    private static final int NUMBER_OF_STONE_TYPES = 3;
    private static final int YELLOW_INDEX = 0;
    private static final int ORANGE_INDEX = 1;
    private static final int RED_INDEX = 2;

    private static final int YELLOW_PROBABILITY = 2;
    private static final int ORANGE_PROBABILITY = 3;
    private static final int RED_PROBABILITY = 4;

    private static final String[] IMAGE_PATHS = {
            "/img/yellow_stone.png",
            "/img/orange_stone.png",
            "/img/red_stone.png"
    };

    private StoneType type;

    /**
     * Creates a philosopher stone at the specified position
     * and randomly assigns its type.
     *
     * @param posX the x-coordinate
     * @param posY the y-coordinate
     */
    public PhilosopherStone(int posX, int posY) {
        super(posX,posY,IMAGE_PATHS);

        generateStoneType();
    }


    /**
     * Returns the image corresponding to the stone type.
     *
     * @return the image path of the current stone
     */
    @Override
    public String getPathToImage() {

        switch(type){

            case YELLOW:
                return getPathToImage(YELLOW_IMAGE_INDEX);

            case ORANGE:
                return getPathToImage(ORANGE_IMAGE_INDEX);

            case RED:
                return getPathToImage(RED_IMAGE_INDEX);
        }

        return getPathToImage(YELLOW_IMAGE_INDEX);
    }

    /**
     * Returns the probability associated with the current stone type.
     * This value is used to determine the effect applied when the
     * stone is collected.
     *
     * @return the probability value
     */
    public int getProbability() {
        switch(type){

            case YELLOW:
                return YELLOW_PROBABILITY;

            case ORANGE:
                return ORANGE_PROBABILITY;

            default:
                return RED_PROBABILITY;
        }
    }

    @Override
    public void triggerAction(HelbFourmizGame game) {}


    /**
     * Randomly selects the type of the philosopher stone.
     */
    private void generateStoneType() {

        Random random = new Random();

        int value = random.nextInt(NUMBER_OF_STONE_TYPES);

        switch(value){

            case YELLOW_INDEX:
                type = StoneType.YELLOW;
                break;

            case ORANGE_INDEX:
                type = StoneType.ORANGE;
                break;

            default:
                type = StoneType.RED;
        }
    }
}
