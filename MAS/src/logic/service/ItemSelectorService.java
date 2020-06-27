package logic.service;

import control.GuiLauncher;
import control.ItemController;
import control.ItemSelectorController;
import dataAccess.ClothingDao;
import dataAccess.CustomerDao;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import logic.agents.chatbotAgent.ChatbotAgent;
import logic.agents.guiAgent.GuiAgent;
import logic.agents.recommenderAgent.RecommenderAgent;
import logic.transfer.Clothing;
import logic.transfer.Customer;

import java.io.IOException;
import java.util.ArrayList;

public class ItemSelectorService implements IService{

    private Customer customer;
    private ArrayList<Clothing> clothings;
    private ItemSelectorController controller;
    private GuiAgent guiAgent;

    public ItemSelectorService(){
        customer = new Customer();
        clothings = new ArrayList<>();
        clothings.add(new Clothing());
    }

    public ItemSelectorService(Customer customer, ItemSelectorController controller, GuiAgent guiAgent) {
        this.customer = customer;
        this.controller = controller;
        this.guiAgent = guiAgent;
        ClothingDao dao = new ClothingDao();
        this.clothings = new ArrayList<>(dao.getAll());
    }

    public int getNumberItems(){
        return clothings.size();
    }

    public Clothing getItem(int i){
        if(i < 0 || i >= clothings.size())
            return new Clothing();

        return clothings.get(i);
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void takeDown() {
        CustomerDao cusDao = new CustomerDao();
        cusDao.write(customer);
    }

    public void onClickItem(int item){
        ItemController con = new ItemController(guiAgent, getItem(item), customer);
        String uri = "../views/item.fxml";

        try {
            takeDown();
            GuiLauncher.instance().switchView(uri, con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickSend(String msg){
        // notify gui agent
        GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND);
        ge.addParameter(msg);
        guiAgent.postGuiEvent(ge);
    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        if(msg.getSender().getName().equals(ChatbotAgent.NAME)){
            String content = msg.getContent();
            //TODO

            controller.showMessage(content);
        }
        else if(msg.getSender().getName().equals(RecommenderAgent.NAME)){
            //TODO
        }
    }
}
