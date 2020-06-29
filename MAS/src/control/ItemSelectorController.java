package control;

import jade.lang.acl.ACLMessage;
import logic.agents.guiAgent.GuiAgent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import logic.service.itemSelectorService.ItemSelectorService;
import logic.transfer.Customer;

public class ItemSelectorController implements AttachableController{
    private static final String ENDL = System.lineSeparator();

    private ItemSelectorService service;
    private int currentIndex = 0;

    @FXML
    private Button _btLeft;
    @FXML
    private Button _btRight;
    @FXML
    private Button _btSend;
    @FXML
    private HBox _hbImages;
    @FXML
    private Label _lbDescription;
    @FXML
    private TextArea _taPrompt;
    @FXML
    private TextField _tfInput;


    public ItemSelectorController(GuiAgent guiAgent, Customer customer){
        this.service = new ItemSelectorService(customer, this, guiAgent);
    }

    public void viewSetup(){
        Platform.runLater(() -> {
            updateClothes();
            onClickImage();
            onClickPager();
            onClickSend();
            service.setup();
        });
    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        service.handleACLMsg(msg);
    }

    private void updateClothes(){
        int size = _hbImages.getChildren().size();
        int[] is = {
                currentIndex-1,
                currentIndex,
                (currentIndex+1 >= service.getNumberItems()) ? -1 : currentIndex+1};

        Image im = null;
        for(int i = 0; i < size; i++){
            if(is[i] == -1) im = null;
            else im = new Image(service.getItem(is[i]).getImageUri());

            ImageView iv = (ImageView)_hbImages.getChildren().get(i);
            iv.setImage(im);
            iv.setSmooth(true);
            iv.setCache(true);
        }

        _lbDescription.setText(
                service.getItem(currentIndex).getPrice() + ENDL +
                service.getItem(currentIndex).getName());
    }

    public void refreshItems(){
        currentIndex = 0;
        updateClothes();
    }

    private void onClickPager() {
        _btRight.setOnMouseClicked(this::pagerAction);
        _btLeft.setOnMouseClicked(this::pagerAction);
    }

    private void pagerAction(MouseEvent ev){
        Platform.runLater(() -> {

            Button bt = (Button) ev.getSource();
            if(bt == _btLeft){
                currentIndex--;
                if(currentIndex == 0)
                    _btLeft.setDisable(true);
            }
            else if(bt == _btRight){
                currentIndex++;
                if(currentIndex == service.getNumberItems()-1)
                    _btRight.setDisable(true);
            }

            updateClothes();

            if(currentIndex > 0)
                _btLeft.setDisable(false);
            if(currentIndex < service.getNumberItems()-1)
                _btRight.setDisable(false);
        });
    }

    private void onClickSend() {
        _btSend.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            String input = _tfInput.getText();
            if(input == null || input.equals(""))
                return;

            StringBuilder sb = new StringBuilder(_taPrompt.getText());
            sb.append("You: ").append(input).append(ENDL);

            _tfInput.setText("");
            _taPrompt.setText(sb.toString());

            service.onClickSend(input);
        }));
    }

    private void onClickImage() {
        _hbImages.getChildren().get(1).setOnMouseClicked(
                mouseEvent -> Platform.runLater(() -> service.onClickItem(currentIndex)));
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
