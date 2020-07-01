package logic.service;

import control.GuiLauncher;
import control.ItemSelectorController;
import dataAccess.ClothingDao;
import dataAccess.CustomerDao;
import jade.lang.acl.ACLMessage;
import logic.agents.guiAgent.GuiAgent;
import logic.transfer.Clothing;
import logic.transfer.Customer;

import java.io.File;
import java.io.IOException;

public class ItemService implements IService{
    private static final String BACK_VIEW = ".." + File.separator + "views" + File.separator + "item-selector.fxml";
    private Clothing clothing;
    private Customer customer;
    private GuiAgent guiAgent;

    public ItemService() {
        this.clothing = new Clothing();
        this.customer = new Customer();
    }

    public ItemService(Clothing clothing, Customer customer, GuiAgent guiAgent) {
        this.clothing = clothing;
        this.customer = customer;
        this.guiAgent = guiAgent;
        addNewView();
    }

    public void addNewView(){
        clothing.addNewView();
        customer.addNewView(clothing.getId());
    }

    public void addNewSale(){
        clothing.addNewSale();
        customer.addNewPurchase(clothing.getId());
    }

    public Clothing getClothing() {
        return clothing;
    }

    @Override
    public void takeDown() {
        ClothingDao clothingDao = new ClothingDao();
        clothingDao.write(clothing);
        CustomerDao customerDao = new CustomerDao();
        customerDao.write(customer);
    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        // no actions are needed in this service
    }

    public void goBack(){
        ItemSelectorController con = new ItemSelectorController(guiAgent, customer);

        try {
            takeDown();
            GuiLauncher.instance().switchView(BACK_VIEW, con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
