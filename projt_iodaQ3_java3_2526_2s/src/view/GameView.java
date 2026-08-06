package view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.Map;

import models.Cell;
import models.GameElement;
import models.HelbFourmizGame;
import static util.GameConfig.*;

/**
 * Displays the graphical representation of the game.
 * This class is responsible for rendering the game board and all
 * game elements based on the current state of the model.
 */
public class GameView {

    private static final int GAME_OVER_FONT_SIZE = 70;
    private static final double GAME_OVER_X_RATIO = 3.5;
    private static final int CENTER_DIVISOR = 2;

    private Stage stage;
    private Scene scene;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;

    private Map<String, Image> imageMap;
    private HelbFourmizGame model;

    public GameView(Stage primaryStage, HelbFourmizGame model){
        this.stage = primaryStage;
        this.model = model;
        this.root = new Group();
        this.imageMap = new HashMap<>();

        initializeView();
        drawGameElements();
    }

    //GETTER
    public Scene getScene() { return scene; }

    /**
     * Refreshes the entire game display.
     * Reloads the images, redraws the background,
     * and renders all game elements.
     */
    public void refreshGame() {
        loadGameImages();
        drawBackground();
        drawGameElements();
    }

    /**
     * Displays the "Game Over" message when the game has ended.
     * Nothing is drawn if the game is still running.
     */
    public void showGameOverScreen() {
        if (model.isGameOver()) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", GAME_OVER_FONT_SIZE));
            gc.fillText("Game Over", WIDTH / GAME_OVER_X_RATIO, HEIGHT / CENTER_DIVISOR);
            return;
        }
    }

    /**
     * Initializes the graphical view by preparing the resources needed
     * for rendering and drawing the initial game state.
     */
    private void initializeView() {
        stage.setTitle("Helb Fourmiz Game");
        canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        gc = canvas.getGraphicsContext2D();

        loadGameImages();
        drawBackground();
    }

    /**
     * Draws the game board by filling each cell with its corresponding color.
     * The color is determined from the RGB values stored in each cell, allowing
     * the background to display movement traces and terrain information.
     */
    private void drawBackground() {

        Cell[][] cells = model.getGameGrid().getCells();

        for (int row = START_INDEX; row < cells.length; row++) {
            for (int col = START_INDEX; col < cells[row].length; col++) {
                Cell cell = cells[row][col];
                Color color = Color.rgb(cell.getRedColorTrace(),cell.getGreenColor(),cell.getBlueColorTrace());
                gc.setFill(color);
                gc.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    /**
     * Draws all game elements (such as ants, resources, or the anthill)
     */
    private void drawGameElements() {
        for(GameElement element : model.getGameElementList()) {
            gc.drawImage(
                    imageMap.get(element.getClass().getName()),
                    element.getPosX() * SQUARE_SIZE,
                    element.getPosY() * SQUARE_SIZE,
                    SQUARE_SIZE, SQUARE_SIZE
            );
        }
    }

    /**
     * Loads the images associated with each type of game element.
     * Images are loaded once and cached in a map to improve rendering performance.
     */
    private void loadGameImages() {
        for(GameElement element : model.getGameElementList()) {
            imageMap.put(element.getClass().getName(), new Image(element.getPathToImage()));
        }
    }
}
