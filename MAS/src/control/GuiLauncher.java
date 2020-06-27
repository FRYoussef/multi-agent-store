package control;

import jade.lang.acl.ACLMessage;
import logic.agents.guiAgent.GuiAgent;
import jade.gui.GuiEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.transfer.Customer;

import java.io.IOException;

public class GuiLauncher extends Application {
    private static GuiLauncher guiLauncher;
    private static Stage primaryStage;
    private static GuiAgent guiAgent;
    private static AttachableController controller;

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
        controller = new ItemSelectorController(guiAgent, new Customer());
        switchView("../views/item-selector.fxml", controller);
    }

    public void switchView(String viewUri, AttachableController controller) throws IOException {
        GuiLauncher.controller = controller;
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
                controller.takeDown();
                GuiEvent ev = new GuiEvent(this, GuiAgent.CMD_EXIT);
                guiAgent.postGuiEvent(ev);
                Platform.exit();
            }
        });
    }

    public void setup(GuiAgent guiAgent){
        GuiLauncher.guiAgent = guiAgent;
        Application.launch();
    }

    public void handleACLMsg(ACLMessage msg){
        controller.handleACLMsg(msg);
    }
}
