package control;

import jade.lang.acl.ACLMessage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import logic.agents.guiAgent.GuiAgent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import logic.service.itemSelectorService.ItemSelectorService;
import logic.transfer.Customer;

import java.util.function.Function;

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
    @FXML
    private ToggleButton _tbAll;
    @FXML
    private ToggleButton _tbRecommendation;
    private final ToggleGroup _tgGroup;


    public ItemSelectorController(GuiAgent guiAgent, Customer customer){
        this.service = new ItemSelectorService(customer, this, guiAgent);
        _tgGroup = new ToggleGroup();
    }

    public void viewSetup(){
        Platform.runLater(() -> {
            updateClothes();
            onClickImage();
            onClickPager();
            onClickSend();
            onClickToggle();
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

        Platform.runLater(() -> {
            _btRight.setDisable(is[2] == -1);

            _lbDescription.setText(
                    service.getItem(currentIndex).getPrice() + ENDL +
                            service.getItem(currentIndex).getName());
        });
    }

    public void refreshItems(){
        _tbAll.setDisable(false);
        _tbRecommendation.setDisable(false);
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
        _tfInput.setOnAction(actionEvent -> Platform.runLater(this::send));
        _btSend.setOnMouseClicked(mouseEvent -> Platform.runLater(this::send));
    }

    private void send(){
        String input = _tfInput.getText();
        if (input == null || input.equals(""))
            return;

        StringBuilder sb = new StringBuilder(_taPrompt.getText());
        sb.append("You: ").append(input).append(ENDL);

        _tfInput.setText("");
        _taPrompt.setText(sb.toString());

        service.onClickSend(input);
    }

    private void onClickImage() {
        _hbImages.getChildren().get(1).setOnMouseClicked(
                mouseEvent -> Platform.runLater(() -> service.onClickItem(currentIndex)));
    }

    private void onClickToggle() {
        Platform.runLater(() -> {
            _tbAll.setToggleGroup(_tgGroup);
            _tbAll.setDisable(true);
            _tbRecommendation.setDisable(true);
            _tbRecommendation.setToggleGroup(_tgGroup);

            _tgGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> service.switchCloths());
        });
    }

    public void selectRecommendationToggle(){
        Platform.runLater(() -> {
            _tgGroup.selectToggle(_tbRecommendation);
        });
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
