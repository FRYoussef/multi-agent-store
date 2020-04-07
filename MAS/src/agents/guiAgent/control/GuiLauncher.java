package agents.guiAgent.control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiLauncher extends Application {

    static final int W_HEIGHT = 700;
    static final int W_WIDTH = 1000;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/main-view.fxml"));
        primaryStage.setTitle("MAS");
        primaryStage.setScene(new Scene(root, W_WIDTH, W_HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
