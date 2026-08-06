package controller;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import models.HelbFourmizGame;
import view.GameView;


/**
 * Controls the interaction between the game model and the view.
 * This class initializes the game, starts the game loop,
 * handles user input, and updates the view according
 * to the current game state.
 */
public class GameController {
    private static final int GAME_SPEED = 500;

    private final HelbFourmizGame model;
    private final GameView view;

    private final Stage stage;
    private Timeline timeline;


    /**
     * Creates a new game controller.
     * Initializes the game model, creates the graphical view,
     * registers keyboard controls, and starts the game loop.
     *
     * @param primaryStage the main JavaFX application window
     */
    public GameController(Stage primaryStage) {
        this.stage = primaryStage;
        this.model = new HelbFourmizGame();
        model.initializeGame();

        this.view = new GameView(primaryStage, model);
        handleCheatCodes(view.getScene());

        timeline = new Timeline(new KeyFrame(Duration.millis(GAME_SPEED), e -> run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Executes one game update.
     * If the game is over, the animation is stopped and the
     * game over screen is displayed. Otherwise, the model
     * is updated and the view is refreshed.
     */
    private void run() {

        if (model.isGameOver()) {
            timeline.stop();
            view.showGameOverScreen();
            return;
        }

        model.run();
        view.refreshGame();
    }

    /**
     * Registers the keyboard controls used during the game.
     * Some keys trigger debug or cheat actions such as spawning
     * game elements, freezing entities, resetting trails,
     * or saving the game.
     *
     * @param scene the scene that receives keyboard events
     */
    private void handleCheatCodes(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                KeyCode code = event.getCode();

                switch (code) {

                    case A:
                        model.generateRedAnt();
                        break;

                    case Z:
                        model.generateBlueAnt();
                        break;

                    case E:
                        model.generateResource();
                        break;

                    case R:
                        model.generateSeed();
                        break;

                    case W:
                        model.freezeRedAnts();
                        break;

                    case X:
                        model.freezeBlueAnts();
                        break;

                    case C:
                        model.freezeSeeds();
                        break;

                    case V:
                        model.freezeAllMovables();
                        break;

                    case J:
                        model.collectAllResources();
                        break;

                    case K:
                        model.returnAllAntsToCamp();
                        break;

                    case L:
                        model.sendAllAntsToResources();
                        break;

                    case M:
                        model.saveGame();
                        break;

                    case U:
                        model.generatePhilosopherStone();
                        break;

                    case I:
                        model.getGameGrid().resetRedTrails();
                        break;

                    case O:
                        model.getGameGrid().resetBlueTrails();
                        break;

                    case P:
                        model.resetAllTrails();
                        break;

                    default:
                        break;
                }
            }
        });
    }
}
