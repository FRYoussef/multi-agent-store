package logic.service;

import control.GuiLauncher;
import control.LoginController;
import control.RegisterController;
import dataAccess.CustomerDao;
import jade.lang.acl.ACLMessage;
import logic.agents.guiAgent.GuiAgent;
import logic.transfer.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class RegisterService implements IService{
    private GuiAgent guiAgent;
    private Customer newCustomer;
    private ArrayList<Customer> customers;

    public RegisterService(GuiAgent guiAgent, ArrayList<Customer> customers) {
        this.guiAgent = guiAgent;
        this.customers = customers;
    }

    public boolean checkUser(String user, String check){
        boolean valid = true;

        if(user == null || user.equals(""))
            return false;

        if(!user.equals(check))
            return false;

        for(Customer c : customers)
            if(c.getName().equals(user)){
                valid = false;
                break;
            }

        return valid;
    }

    public boolean checkPassword(String pass, String check){
        if(pass == null || pass.equals(""))
            return false;

        return pass.equals(check);
    }

    public void registerUser(String user, String password){
        newCustomer = new Customer(customers.size(), user, password);
        customers.add(newCustomer);

        LoginController con = new LoginController(guiAgent);
        String uri = "../views/login.fxml";

        try {
            takeDown();
            GuiLauncher.instance().switchView(uri, con);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void goBack(){
        LoginController con = new LoginController(guiAgent);
        String uri = "../views/login.fxml";

        try {
            takeDown();
            GuiLauncher.instance().switchView(uri, con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void takeDown() {
        if(newCustomer != null){
            CustomerDao dao = new CustomerDao();
            dao.writeAll(new TreeSet<>(customers));
        }
    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        // no acl msg expected
    }
}
