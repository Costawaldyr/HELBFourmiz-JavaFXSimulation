package models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import util.Coordinate;
import static util.GameConfig.*;

/**
 * Represents the main game model.
 * This class manages the game state, all game elements, the game loop,
 * and the interactions between ants, resources, seeds, and philosopher stones.
 */
public class HelbFourmizGame {

    private static final int NUMBER_OF_RESOURCES = 6;
    private static final int RANDOM_MIN = 0;

    public static final String SAVE_FILE_NAME = "save.jds";

    private int collectedResources = 0;
    private int nbrRedAnt = 0;
    private int nbrBlueAnt = 0;

    private boolean gameOver;

    private Grid gameGrid;
    private Camp camp;
    private ArrayList<GameElement> gameElementList;
    private ArrayList<Ant> antList;
    private ArrayList<RedAnt> redAntList;
    private ArrayList<BlueAnt> blueAntList;
    private ArrayList<Resource> resourcesList;
    private ArrayList<Seed> seedList;
    private ArrayList<PhilosopherStone> philosopherStoneList;


    /**
     * Creates a new game model and initializes all game collections.
     */
    public HelbFourmizGame() {
        this.gameGrid = new Grid();
        this.camp = new Camp();
        this.gameElementList = new ArrayList<>();
        this.antList = new ArrayList<>();
        this.redAntList = new ArrayList<>();
        this.blueAntList = new ArrayList<>();
        this.resourcesList = new ArrayList<>();
        this.seedList = new ArrayList<>();
        this.philosopherStoneList = new ArrayList<>();
        this.gameOver = false;

        gameElementList.add(camp);
    }

    //GETTERS
    public Grid getGameGrid() { return gameGrid; }
    public Camp getCamp(){return camp;}
    public ArrayList<GameElement> getGameElementList(){return gameElementList;}
    public ArrayList<Ant>getAntList(){return antList; }
    public ArrayList<Resource>getResourceList(){return resourcesList; }
    public ArrayList<Seed> getSeedList() {return seedList;}
    public ArrayList<PhilosopherStone>getPhilosopherStoneList() { return philosopherStoneList; }

    public boolean canGenerateCampAnt() {return antList.size() < NUMBER_OF_RESOURCES;}
    public boolean isGameOver() { return gameOver; }


    /**
     * Initializes the game by creating the initial ants,
     * resources, and philosopher stone.
     */
    public void initializeGame() {
        updateResource();
        generatePhilosopherStone();
    }

    /**
     * Executes one game update.
     * Each game element performs its action during this step.
     * No update is performed if the game has ended.
     */
    public void run() {

        if (gameOver) { return; }

        ArrayList<GameElement> copy = new ArrayList<>(gameElementList);

        for(GameElement e : copy){
            e.triggerAction(this);
        }

    }


    // =======================
    // Ant Management
    // =======================

    /**
     * Adds an ant to the game and updates the corresponding collections.
     */
    public void addAnt(Ant ant) {
        antList.add(ant);
        gameElementList.add(ant);

        if (ant instanceof RedAnt) {
            redAntList.add((RedAnt) ant);
            nbrRedAnt++;
        }

        if (ant instanceof BlueAnt) {
            blueAntList.add((BlueAnt) ant);
            nbrBlueAnt++;
        }
    }

    /**
     * Removes an ant from the game and updates the corresponding collections.
     */
    public void removeAnt(Ant ant){
        antList.remove(ant);
        gameElementList.remove(ant);

        if (ant instanceof RedAnt) {
            redAntList.remove((RedAnt) ant);
            nbrRedAnt--;
        }

        if (ant instanceof BlueAnt) {
            blueAntList.remove((BlueAnt) ant);
            nbrBlueAnt--;
        }
    }

    /**
     * Creates and adds a new blue ant at a random free position.
     */
    public void generateBlueAnt(){
        generateRandomAnt(true);
    }

    /**
     * Creates and adds a new red ant at a random free position.
     */
    public void generateRedAnt(){
        generateRandomAnt(false);
    }

    /**
     * Orders every ant to return to the camp.
     */
    public void returnAllAntsToCamp() {
        for (Ant ant : antList) {
            ant.returnToCamp();
        }
    }

    /**
     * Orders every ant to search for resources.
     */
    public void sendAllAntsToResources() {
        for (Ant ant : antList) {
            ant.searchResources();
        }
    }

    // =======================
    // Resource Management
    // =======================

    /**
     * Adds a resource to the game.
     * @param resource the resource to add
     */
    public void addResource(Resource resource) {
        resourcesList.add(resource);
        gameElementList.add(resource);
    }

    /**
     * Removes a resource from the game.
     * @param resource the resource to remove
     */
    public void removeResource(Resource resource) {
        resourcesList.remove(resource);
        gameElementList.remove(resource);
    }

    /**
     * Creates a resource at a random free position.
     */
    public void generateResource() {
        if (!hasFreeCoordinate()) { return; }
        Coordinate coord = randomFreeCoordinate();
        Resource res = new Resource(coord.x, coord.y);
        addResource(res);
    }

    /**
     * Collects a resource and replaces it with a seed.
     * @param res the collected resource
     */
    public void collectResource(Resource res) {
        removeResource(res);
        trySpawnSeed(res);
    }

    /**
     * Collects every resource currently present on the board.
     */
    public void collectAllResources() {

        ArrayList<Resource> resList = new ArrayList<>(resourcesList);
        for (Resource res : resList) {
            collectResource(res);
        }
    }

    /**
     * Increments the number of collected resources.
     */
    public void depositResource() {
        collectedResources++;
    }


    // =======================
    // Seed Management
    // =======================

    public void addSeed(Seed seed) {
        seedList.add(seed);
        gameElementList.add(seed);
    }

    public void removeSeed(Seed seed) {
        seedList.remove(seed);
        gameElementList.remove(seed);
    }

    /**
     * Creates a new seed at a random position and assigns its destination.
     */
    public void generateSeed(){
        if (!hasFreeCoordinate()) { return; }
        Coordinate coord = randomFreeCoordinate();
        Coordinate spawn = gameGrid.findNearestFreeCoordinate(coord,this);
        Seed seed = new Seed(spawn.x, spawn.y);
        seed.setTarget(gameGrid.findLeastVisitedCoordinate(this,spawn));
        addSeed(seed);
    }

    /**
     * Creates a seed at the position where a resource was collected.
     * @param resource the collected resource
     */
    public void trySpawnSeed(Resource resource) {
        Coordinate origin = resource.getPosition();
        Coordinate spawn =gameGrid.findNearestFreeCoordinate(origin,this);
        Seed seed = new Seed(spawn.x, spawn.y);
        seed.setTarget(gameGrid.findLeastVisitedCoordinate(this, spawn));
        addSeed(seed);
    }

    /**
     * Replaces a mature seed with a new resource.
     * @param seed the seed to germinate
     */
    public void germinateSeed(Seed seed) {
        Coordinate coord = seed.getPosition();
        removeSeed(seed);
        Resource res = new Resource(coord.x,coord.y);
        addResource(res);
    }

    // =======================
    // Philosopher Stone Management
    // =======================

    public void addStone(PhilosopherStone stone) {
        philosopherStoneList.add(stone);
        gameElementList.add(stone);
    }

    public void removeStone(PhilosopherStone stone) {
        philosopherStoneList.remove(stone);
        gameElementList.remove(stone);
    }

    /**
     * Creates a philosopher stone at a random free position.
     */
    public void generatePhilosopherStone(){
        if (!hasFreeCoordinate()) { return; }
        Coordinate coord = randomFreeCoordinate();
        PhilosopherStone stone = new PhilosopherStone(coord.x, coord.y);
        addStone(stone);
    }

   /**
     * Collects the philosopher stone located at the given coordinate
     * and immediately generates a new one.
     *
     * @param coordinate the position of the collected stone
     */
    public void collectStone(Coordinate coordinate) {

        for (PhilosopherStone stone : new ArrayList<>(philosopherStoneList)) {

            if (!stone.getPosition().isSameAs(coordinate)) {
                continue;
            }

            removeStone(stone);
            generatePhilosopherStone();
            return;
        }
    }

    /**
     * Replaces an ant with a resource at the same position.
     * @param ant the ant to transform
     */
    public void transformAntIntoResource(Ant ant) {
        removeAnt(ant);
        Resource res = new Resource(ant.getPosX(), ant.getPosY());
        addResource(res);
    }

    /**
     * Creates a copy of an ant on a free adjacent cell.
     * @param ant the ant to duplicate
     */
    public void duplicateAnt(Ant ant) {

        Coordinate position = ant.getPosition();

        for (Coordinate coord : gameGrid.getAccessibleAdjacentCoordinates(position)) {

            if (!isFreeCoordinate(coord)) { continue; }

            boolean blue = ant instanceof BlueAnt;
            addAnt(createAnt(blue, coord.x, coord.y));
            return;
        }
    }


    // =======================
    // other Management
    // =======================

    public void freezeBlueAnts() {
        for (BlueAnt ant : blueAntList) {
            ant.setFrozen(true);
        }
    }

    public void freezeRedAnts() {
        for (RedAnt ant : redAntList) {
            ant.setFrozen(true);
        }
    }

    public void freezeSeeds() {
        for (Seed seed : seedList) {
            seed.setFrozen(true);
        }
    }

    /**
     * Freezes every movable game element.
     */
    public void freezeAllMovables() {
        freezeBlueAnts();
        freezeRedAnts();
        freezeSeeds();
    }

    /**
     * Removes all red and blue pheromone trails from the grid.
     */
    public void resetAllTrails() {
        gameGrid.resetBlueTrails();
        gameGrid.resetRedTrails();
    }

    /**
     * Saves the game statistics to a file and ends the simulation.
     */
    public void saveGame() {
        try {
            FileWriter myWriter = new FileWriter(SAVE_FILE_NAME);

            myWriter.write("Red Ants : " + nbrRedAnt + "\n");
            myWriter.write("Blue Ants : " + nbrBlueAnt + "\n");
            myWriter.write("Total nbr of resources collected : " + collectedResources + "\n");

            myWriter.close();
            System.out.println("Successfully wrote to the file : " + SAVE_FILE_NAME);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("End of simulation");
        System.out.println("Red Ants : " + nbrRedAnt );
        System.out.println("Blue Ants : " + nbrBlueAnt );
        System.out.println("Total nbr of resources collected : " + collectedResources);

        gameOver = true;
    }

    /**
     * Checks whether a coordinate is inside the board
     * and not occupied by another game element.
     * 
     * @param coordinate the coordinate to test
     * @return true if the coordinate is available
     */
    public boolean isFreeCoordinate(Coordinate coordinate) {

        if (!gameGrid.isCoordinateInBoard(coordinate)) {
            return false;
        }

        for (GameElement element : gameElementList) {
            Coordinate pos = element.getPosition();
            if (pos.x == coordinate.x && pos.y == coordinate.y) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether an ant can move to the given coordinate.
     * Resources and philosopher stones are considered walkable
     * because they can be collected.
     *
     * @param coord  the destination coordinate
     * @param target the ant's current target
     * @return true if the coordinate can be reached
     */
    public boolean isWalkable(Coordinate coord, Coordinate target) {

        if (!gameGrid.isCoordinateInBoard(coord)) {
            return false;
        }

        if (coord.isSameAs(target)) {
            return true;
        }

        for (Resource resource : resourcesList) {
            if (resource.getPosition().isSameAs(coord)) {
                return false;
            }
        }

        for (PhilosopherStone stone : philosopherStoneList) {
            if (stone.getPosition().isSameAs(coord)) {
                return true;
            }
        }

        return isFreeCoordinate(coord);
    }

    /**
     * Checks whether a philosopher stone is located at the given coordinate.
     *
     * @param coordinate the coordinate to test
     * @return true if a philosopher stone is present
     */
    public boolean hasStoneAt(Coordinate coordinate) {
        for (PhilosopherStone stone : philosopherStoneList) {
            if (stone.getPosition().isSameAs(coordinate)) {
                return true;
            }
        }
        return false;
    }



    /**
     * Generates the initial resources at the start of the game.
    */
    private void updateResource() {
        for(int i = START_INDEX; i < NUMBER_OF_RESOURCES; i++){
            generateResource();
        }
    }


    /**
     * Creates a new ant of the specified color.
     *
     * @param blue true to create a blue ant, false for a red ant
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the created ant
     */
    private Ant createAnt(boolean blue, int x, int y) {
        if (blue) {
            return new BlueAnt(x, y);
        }
        return new RedAnt(x, y);
    }


    /**
     * Generates an ant at a random free position.
     * @param blue true to create a blue ant, false for a red ant
     */
    private void generateRandomAnt(boolean blue) {
        if (!hasFreeCoordinate()) { return; }
        Coordinate coord = randomFreeCoordinate();
        addAnt(createAnt(blue, coord.x, coord.y));
    }

    /**
     * Checks whether at least one free coordinate remains on the board.
     *
     * @return true if a free coordinate exists
     */
    private boolean hasFreeCoordinate() {
        for (int row = START_INDEX; row < gameGrid.getRows(); row++) {
            for (int col = START_INDEX; col < gameGrid.getColumns(); col++) {
                Coordinate coord = new Coordinate(col, row);
                if (isFreeCoordinate(coord)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds a random unoccupied coordinate on the game board.
     * @return a free coordinate
     */
    private Coordinate randomFreeCoordinate() {
        start:
        while (true) {
            int x = RANDOM_MIN + (int) (Math.random() * gameGrid.getColumns());
            int y = RANDOM_MIN + (int) (Math.random() * gameGrid.getRows());

            Coordinate coord = new Coordinate(x, y);
            if (!isFreeCoordinate(coord)) {
                continue start;
            }
            return coord;
        }
    }
}
