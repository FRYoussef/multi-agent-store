package agents.guiAgent.control;

import agents.chatbotAgent.ChatbotAgent;
import agents.guiAgent.GuiAgent;
import jade.gui.GuiEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Clothing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ItemController implements AttachableController{
    @FXML
    private Button _btBack;
    @FXML
    private Button _btBuy;
    @FXML
    private Button _btSend;
    @FXML
    private TextArea _taPrompt;
    @FXML
    private TextField _tfInput;
    @FXML
    private ImageView _ivImage;
    @FXML
    private Label _lbPrice;
    @FXML
    private Label _lbTitle;
    @FXML
    private ChoiceBox _cbSize;

    private static final String ENDL = System.lineSeparator();
    private GuiAgent guiAgent;
    private ChatbotAgent chatbotAgent;
    private Clothing item;

    public ItemController(GuiAgent guiAgent, ChatbotAgent chatbotAgent, Clothing item) {
        this.guiAgent = guiAgent;
        this.chatbotAgent = chatbotAgent;
        this.item = item;
    }

    @Override
    public void viewSetup() {
        Platform.runLater(() -> {
            // assign events
            onClickBack();
            onClickSend();
            onClickBuy();

            // Show image, description and size
            Image im = new Image(item.getImageUri());
            _ivImage.setImage(im);

            _lbTitle.setText(item.getName());
            _lbPrice.setText("Price: " + item.getPrice());

            if(item.getSizes().length == 0)
                _cbSize.setDisable(true);
            _cbSize.setItems(FXCollections.observableList(new ArrayList<>(Arrays.asList(item.getSizes()))));
        });
    }

    private void onClickBuy(){
        _btBuy.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            //TODO
            System.out.println("You have bought it");
        }));
    }

    private void onClickSend() {
        _btSend.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            String input = _tfInput.getText();
            StringBuilder sb = new StringBuilder(_taPrompt.getText());
            sb.append("You: ").append(input).append(ENDL);

            // notify gui agent
            GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND);
            ge.addParameter(input);
            guiAgent.postGuiEvent(ge);

            _tfInput.setText("");
            _taPrompt.setText(sb.toString());
        }));
    }

    private void onClickBack(){
        _btBack.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            MainController con = new MainController(guiAgent);
            String uri = "../views/main-view.fxml";
            try {
                GuiLauncher.instance().switchView(uri, con);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    public void showMessage(String msg) {
        Platform.runLater(() -> {
            _taPrompt.setText(
                    _taPrompt.getText() +
                            "ChatBot: " +
                            msg + ENDL
            );
        });
    }
}
