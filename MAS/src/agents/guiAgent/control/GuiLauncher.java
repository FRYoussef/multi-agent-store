package agents.guiAgent.control;

import agents.guiAgent.GuiAgent;
import jade.gui.GuiEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GuiLauncher extends Application {
    static final int W_HEIGHT = 700;
    static final int W_WIDTH = 1000;
    private static GuiLauncher guiLauncher;
    private static Stage primaryStage;
    private static GuiAgent agent;

    /**
     * Singelton
     * @return guiLauncher
     */
    public static GuiLauncher instance(){
        if(guiLauncher == null)
            guiLauncher = new GuiLauncher();

        return guiLauncher;
    }

    @Override
    public void start(Stage stage){
        primaryStage = stage;
        switchView("../views/main-view.fxml", null);
    }

    private void switchView(String viewUri, Object controller){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewUri));
            Parent root = loader.load();
            primaryStage.setTitle("MAS");
            primaryStage.setScene(new Scene(root, W_WIDTH, W_HEIGHT));
            primaryStage.setResizable(false);

            if(controller == null){
                MainController con = loader.getController();
                con.setup(agent);
            }
            else
                loader.setController(controller);

            primaryStage.show();
            primaryStage.setOnCloseRequest(new EventHandler<>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    GuiEvent ev = new GuiEvent(this, GuiAgent.CMD_EXIT);
                    agent.postGuiEvent(ev);
                    Platform.exit();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup(GuiAgent guiAgent){
        agent = guiAgent;
        Application.launch();
    }
}
