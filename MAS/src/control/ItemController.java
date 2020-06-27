package control;

import jade.lang.acl.ACLMessage;
import logic.agents.guiAgent.GuiAgent;
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
    private TextArea _taPrompt;
    @FXML
    private ImageView _ivImage;
    @FXML
    private Label _lbPrice;
    @FXML
    private Label _lbTitle;
    @FXML
    private ChoiceBox _cbSize;

    private static final String ENDL = System.lineSeparator();
    private ItemService service;

    public ItemController(GuiAgent guiAgent, Clothing item, Customer customer) {
        this.service = new ItemService(item, customer, guiAgent);
    }

    @Override
    public void viewSetup() {
        Platform.runLater(() -> {
            // assign events
            onClickBack();
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

    @Override
    public void handleACLMsg(ACLMessage msg) {
        service.handleACLMsg(msg);
    }

    private void onClickBuy(){
        _btBuy.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            String msg;
            String size = (String) _cbSize.getSelectionModel().getSelectedItem();

            if(size == null){
                msg = "Please select a size.";
                showMessage(msg);
                return;
            }

            service.addNewSale();
            msg = "You bought: " + "\""
                  + service.getClothing().getName()
                  + "\" with size \"" + size + "\""
                  + " for " + service.getClothing().getPrice();
            showMessage(msg);
        }));
    }

    private void onClickBack(){
        _btBack.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> service.goBack()));
    }

    public void showMessage(String msg) {
        Platform.runLater(() -> _taPrompt.setText(_taPrompt.getText() + msg + ENDL));
    }

    @Override
    public void takeDown() {
        service.takeDown();
    }
}
