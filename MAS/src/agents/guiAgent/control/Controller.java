package agents.guiAgent.control;

import agents.guiAgent.GuiAgent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Controller {
    private static final String ENDL = System.lineSeparator();

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
        Platform.runLater(() -> {
            String input = _tfInput.getText();
            StringBuilder sb = new StringBuilder(_taPrompt.getText());
            sb.append("You: ").append(input).append(ENDL);

            // just for test, remove when chatbot is deployed
            sb.append("ChatBot: Test").append(ENDL);

            _tfInput.setText("");
            _taPrompt.setText(sb.toString());
        });
    }
}
