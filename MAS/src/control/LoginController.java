package control;

import jade.lang.acl.ACLMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import logic.agents.guiAgent.GuiAgent;
import logic.service.LoginService;

public class LoginController implements AttachableController{
    @FXML
    private Button _btLogin;
    @FXML
    private Button _btRegister;
    @FXML
    private TextField _tfUser;
    @FXML
    private TextField _tfPassword;

    private final LoginService service;

    public LoginController(GuiAgent guiAgent) {
        service = new LoginService(guiAgent);
    }

    @Override
    public void viewSetup() {
        Platform.runLater(() -> {
            onClickLogin();
            onClickRegister();
        });
    }

    private void onClickLogin(){
        _tfUser.setOnAction(actionEvent -> Platform.runLater(this::login));
        _tfPassword.setOnAction(actionEvent -> Platform.runLater(this::login));
        _btLogin.setOnMouseClicked(mouseEvent -> Platform.runLater(this::login));
    }

    private void login(){
        _tfUser.setStyle("-fx-text-inner-color: black;");
        _tfPassword.setStyle("-fx-text-inner-color: black;");

        String user = _tfUser.getText();
        if(!service.isValidUser(user)){
            _tfUser.setStyle("-fx-text-inner-color: red;");
            return;
        }
        String password = _tfPassword.getText();
        if(!service.isPasswordValid(password)){
            _tfPassword.setStyle("-fx-text-inner-color: red;");
            return;
        }

        disable();
        service.loginComplete();
    }

    private void disable(){
        _btLogin.setDisable(true);
        _btRegister.setDisable(true);
        _tfUser.setDisable(true);
        _tfPassword.setDisable(true);
    }

    private void onClickRegister(){
        _btRegister.setOnMouseClicked(mouseEvent -> Platform.runLater(service::onClickRegister));
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
