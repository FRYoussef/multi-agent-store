package logic.service;

import control.GuiLauncher;
import control.ItemSelectorController;
import control.RegisterController;
import dataAccess.CustomerDao;
import jade.lang.acl.ACLMessage;
import logic.agents.guiAgent.GuiAgent;
import logic.transfer.Customer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LoginService implements IService{
    private static final String NEXT_VIEW = ".." + File.separator + "views" + File.separator + "item-selector.fxml";
    private static final String REGISTER_VIEW = ".." + File.separator + "views" + File.separator + "register.fxml";
    private GuiAgent guiAgent;
    private ArrayList<Customer> customers;
    private Customer customer;

    public LoginService(GuiAgent guiAgent) {
        this.guiAgent = guiAgent;
        CustomerDao dao = new CustomerDao();
        customers = new ArrayList<>(dao.getAll());
    }

    public boolean isValidUser(String user){
        if(user == null)
            return false;

        boolean valid = false;

        for(Customer c : customers)
            if(c.getName().equals(user)){
                valid = true;
                customer = c;
                break;
            }

        return valid;
    }

    public boolean isPasswordValid(String password){
        if(password == null)
            return false;
        if(customer == null)
            return false;

        return customer.isValidPassword(password);
    }

    public void loginComplete(){
        if(customer == null)
            return;

        ItemSelectorController con = new ItemSelectorController(guiAgent, customer);

        try {
            takeDown();
            GuiLauncher.instance().switchView(NEXT_VIEW, con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickRegister(){
        RegisterController con = new RegisterController(guiAgent);

        try {
            takeDown();
            GuiLauncher.instance().switchView(REGISTER_VIEW, con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void takeDown() {
        // nothing to save
    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        // no acml msg expected. They are ignored.
    }
}
