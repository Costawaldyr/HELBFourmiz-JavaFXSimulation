import controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
* Main entry point for the game application
 * Initializes the GameController
*/
public class Main extends Application{

    private GameController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        controller = new GameController(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}