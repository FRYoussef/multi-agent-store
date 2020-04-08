package agents.guiAgent.control;

import agents.guiAgent.GuiAgent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.ClothTransfer;

import java.io.IOException;

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
    private MenuButton _mbSize;

    private static final String ENDL = System.lineSeparator();
    private static final String LOCATION = "/resources/";
    private GuiAgent agent;
    private ClothTransfer item;

    public ItemController(GuiAgent agent, ClothTransfer item) {
        this.agent = agent;
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
            Image im = new Image(LOCATION + item.getImageUri());
            _ivImage.setImage(im);

            _lbTitle.setText(item.getName());
            _lbPrice.setText("Price: " + item.getPrice());

            //TODO add sizes to menu button

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

            // it's just for test, remove it when chatbot is deployed
            sb.append("ChatBot: Test").append(ENDL);

            _tfInput.setText("");
            _taPrompt.setText(sb.toString());
        }));
    }

    private void onClickBack(){
        _btBack.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            MainController con = new MainController(agent);
            String uri = "../views/main-view.fxml";
            try {
                GuiLauncher.instance().switchView(uri, con);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
