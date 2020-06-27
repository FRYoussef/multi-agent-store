package control;

import jade.lang.acl.ACLMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import logic.agents.guiAgent.GuiAgent;
import logic.service.RegisterService;
import logic.transfer.Customer;

import java.util.ArrayList;

public class RegisterController implements AttachableController{
    @FXML
    private Button _btBack;
    @FXML
    private Button _btCreate;
    @FXML
    private TextField _tfNUser;
    @FXML
    private TextField _tfCNUser;
    @FXML
    private TextField _tfPassword;
    @FXML
    private TextField _tfCPassword;

    private final RegisterService service;

    public RegisterController(GuiAgent guiAgent) {
        service = new RegisterService(guiAgent);
    }

    @Override
    public void viewSetup() {
        Platform.runLater(() -> {
            onClickBack();
            onClickCreate();
        });
    }

    private void onClickBack(){
        _btBack.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> service.goBack()));
    }

    private void onClickCreate(){
        _btCreate.setOnMouseClicked(mouseEvent -> Platform.runLater(() -> {
            _tfNUser.setStyle("-fx-text-inner-color: black;");
            _tfCNUser.setStyle("-fx-text-inner-color: black;");
            _tfPassword.setStyle("-fx-text-inner-color: black;");
            _tfCPassword.setStyle("-fx-text-inner-color: black;");

            String user = _tfNUser.getText();
            String cUser = _tfCNUser.getText();

            if(!service.checkUser(user, cUser)){
                _tfNUser.setStyle("-fx-text-inner-color: red;");
                _tfCNUser.setStyle("-fx-text-inner-color: red;");
                return;
            }

            String pass = _tfPassword.getText();
            String cPass = _tfCPassword.getText();

            if(!service.checkPassword(pass, cPass)){
                _tfPassword.setStyle("-fx-text-inner-color: red;");
                _tfCPassword.setStyle("-fx-text-inner-color: red;");
                return;
            }

            service.registerUser(user, pass);
        }));
    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        service.handleACLMsg(msg);
    }

    @Override
    public void takeDown() {
        service.takeDown();
    }
}
