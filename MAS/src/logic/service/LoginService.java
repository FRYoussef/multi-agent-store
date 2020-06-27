package logic.service;

import control.GuiLauncher;
import control.ItemSelectorController;
import dataAccess.CustomerDao;
import jade.lang.acl.ACLMessage;
import logic.agents.guiAgent.GuiAgent;
import logic.transfer.Customer;

import java.io.IOException;
import java.util.ArrayList;

public class LoginService implements IService{
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
        String uri = "../views/item-selector.fxml";

        try {
            takeDown();
            GuiLauncher.instance().switchView(uri, con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickRegister(){
        // TODO

        ItemSelectorController con = new ItemSelectorController(guiAgent, customer);
        String uri = "../views/item-selector.fxml";

        try {
            takeDown();
            GuiLauncher.instance().switchView(uri, con);
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
