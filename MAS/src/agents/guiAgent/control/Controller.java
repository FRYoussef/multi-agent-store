package agents.guiAgent.control;

import agents.guiAgent.GuiAgent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Controller {
    private GuiAgent agent;

    @FXML
    private Button _btLeft;
    @FXML
    private Button _btRight;
    @FXML
    private HBox _hbImages;
    @FXML
    private Label _lbDescription;
    @FXML
    private TextArea _taPrompt;
    @FXML
    private TextField _tfInput;


    public void setAgent(GuiAgent agent) {
        this.agent = agent;
    }


    public void onClickPager(ActionEvent actionEvent) {

    }

    public void onClickSend(ActionEvent actionEvent) {

    }
}
