package control;

import logic.agents.chatbotAgent.ChatbotAgent;
import logic.agents.guiAgent.GuiAgent;
import dataAccess.ClothingDao;
import jade.gui.GuiEvent;
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
import logic.transfer.Clothing;

import java.io.IOException;
import java.util.ArrayList;

public class MainController implements AttachableController{
    private static final String ENDL = System.lineSeparator();

    private GuiAgent guiAgent;
    private ChatbotAgent chatbotAgent;
    private ArrayList<Clothing> alClothes;
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


    public MainController(GuiAgent guiAgent){
        this.guiAgent = guiAgent;
        ClothingDao dao = new ClothingDao();
        alClothes = new ArrayList<>(dao.getAll());
    }

    public void viewSetup(){
        Platform.runLater(() -> {
            updateClothes();
            onClickImage();
            onClickPager();
            onClickSend();
        });
    }

    private void updateClothes(){
        int size = _hbImages.getChildren().size();
        int[] is = {
                currentIndex-1,
                currentIndex,
                (currentIndex+1 >= alClothes.size()) ? -1 : currentIndex+1};

        Image im = null;
        for(int i = 0; i < size; i++){
            if(is[i] == -1) im = null;
            else im = new Image(alClothes.get(is[i]).getImageUri());

            ImageView iv = (ImageView)_hbImages.getChildren().get(i);
            iv.setImage(im);
            iv.setSmooth(true);
            iv.setCache(true);
        }

        _lbDescription.setText(
                alClothes.get(currentIndex).getPrice() +
                ENDL +
                alClothes.get(currentIndex).getName());
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
                if(currentIndex == alClothes.size()-1)
                    _btRight.setDisable(true);
            }

            updateClothes();

            if(currentIndex > 0)
                _btLeft.setDisable(false);
            if(currentIndex < alClothes.size()-1)
                _btRight.setDisable(false);
        });
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

    private void onClickImage() {
        _hbImages.getChildren().get(1).setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            ItemController con = new ItemController(guiAgent, chatbotAgent, alClothes.get(currentIndex));
            String uri = "../views/item.fxml";

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
