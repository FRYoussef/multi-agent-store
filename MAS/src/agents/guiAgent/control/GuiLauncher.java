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
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        switchView("../views/main-view.fxml", new MainController(agent));
    }

    public void switchView(String viewUri, AttachableController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewUri));
        primaryStage.setTitle("MAS");
        primaryStage.setResizable(false);
        loader.setController(controller);
        controller.viewSetup();
        primaryStage.setScene(new Scene(loader.load()));

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

    public void setup(GuiAgent guiAgent){
        agent = guiAgent;
        Application.launch();
    }
}
