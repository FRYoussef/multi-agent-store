package control;

import logic.agents.chatbotAgent.ChatbotAgent;
import logic.agents.guiAgent.GuiAgent;
import jade.gui.GuiEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.service.ItemService;
import logic.transfer.Clothing;
import logic.transfer.Customer;

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
    private ItemService service;

    public ItemController(GuiAgent guiAgent, Clothing item, Customer customer) {
        this.guiAgent = guiAgent;
        this.service = new ItemService(item, customer);
    }

    @Override
    public void viewSetup() {
        Platform.runLater(() -> {
            // assign events
            onClickBack();
            onClickSend();
            onClickBuy();

            Clothing item = service.getClothing();
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
            service.addNewSale();
            Platform.runLater(() -> _taPrompt.setText(
                    _taPrompt.getText() + "You bought: " + service.getClothing().getName() + ENDL
            ));
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
            ItemSelectorController con = new ItemSelectorController(guiAgent, service.getCustomer());
            String uri = "../views/item-selector.fxml";
            try {
                takeDown();
                GuiLauncher.instance().switchView(uri, con);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    public void showMessage(String msg) {
        Platform.runLater(() -> _taPrompt.setText(
                _taPrompt.getText() +
                        "ChatBot: " +
                        msg + ENDL
        ));
    }

    @Override
    public void takeDown() {
        service.takeDown();
    }
}
