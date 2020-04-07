package agents.guiAgent.control;

import agents.guiAgent.GuiAgent;
import jade.gui.GuiEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {
    private GuiAgent agent;

    public void setAgent(GuiAgent agent) {
        this.agent = agent;
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        GuiEvent ev = new GuiEvent(this, GuiAgent.CMD_EXIT);
        agent.postGuiEvent(ev);
        Platform.exit();
    }
}
