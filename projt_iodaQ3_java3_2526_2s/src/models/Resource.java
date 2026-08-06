package models;

/**
 * Represents a resource that can be collected by ants.
 * Once collected, it is replaced by a seed.
 */
public class Resource extends GameElement {

    private static final String[] RESOURCE_IMAGE_PATH = {"/img/resources.png"};

    /**
     * Creates a new resource at the specified position.
     *
     * @param posX the x-coordinate of the resource
     * @param posY the y-coordinate of the resource
     */
    public Resource(int posX, int posY) {
        super(posX,posY,RESOURCE_IMAGE_PATH);
    }

    @Override
    public void triggerAction(HelbFourmizGame gameBoard) {}
}
