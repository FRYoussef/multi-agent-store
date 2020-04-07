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

public class GuiLauncher extends Application {
    static final int W_HEIGHT = 700;
    static final int W_WIDTH = 1000;
    static GuiAgent agent;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/main-view.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("MAS");
        primaryStage.setScene(new Scene(root, W_WIDTH, W_HEIGHT));

        Controller con = loader.getController();
        con.setup(agent);

        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                GuiEvent ev = new GuiEvent(this, GuiAgent.CMD_EXIT);
                agent.postGuiEvent(ev);
                Platform.exit();
            }
        });
    }

    public static void addAgent(GuiAgent guiAgent){
        agent = guiAgent;
    }
}
