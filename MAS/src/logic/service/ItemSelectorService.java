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
import logic.agents.recommenderAgent.RecommenderMsg;
import logic.agents.recommenderAgent.pythonArgsAdapter.ContentBasedAdapter;
import logic.transfer.Clothing;
import logic.transfer.Customer;

import java.io.IOException;
import java.util.ArrayList;

public class ItemSelectorService implements IService{

    private static final String ERROR_RECOMMENDATION = "error";
    private Customer customer;
    private ArrayList<Clothing> clothings;
    private ArrayList<Clothing> recomendations;
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
        ArrayList<Clothing> cs = (recomendations == null) ? clothings : recomendations;
        return cs.size();
    }

    public Clothing getItem(int i){
        ArrayList<Clothing> cs = (recomendations == null) ? clothings : recomendations;

        if(i < 0 || i >= cs.size())
            return new Clothing();

        return cs.get(i);
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
        // TODO uncomment for chatbot interaction
       /* // notify gui agent
        GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_CHATBOT);
        ge.addParameter(msg);
        guiAgent.postGuiEvent(ge);
        */


        // TODO comment. It's just for testing
        customer.addPreference("blue");
        customer.setGender("male");
        ContentBasedAdapter adapter = new ContentBasedAdapter(customer);
        RecommenderMsg rMsg = new RecommenderMsg(RecommenderMsg.CONTENT_BASED_TYPE, adapter.getPythonArgs());
        GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_RECOMMENDER);
        ge.addParameter(rMsg.getRawMsg());
        guiAgent.postGuiEvent(ge);
    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        if(msg.getSender().getName().contains(ChatbotAgent.NAME))
            handleChatbotMsg(msg.getContent());

        else if(msg.getSender().getName().contains(RecommenderAgent.NAME))
            handleRecommenderMsg(msg.getContent());
    }

    private void handleChatbotMsg(String msg) {
        //TODO

        controller.showMessage(msg);
    }

    private void handleRecommenderMsg(String msg){
        if(msg.equals("")) {
            controller.showMessage("I don't found recommendations for you, sorry :(");
            return;
        }
        else if(msg.equals(ERROR_RECOMMENDATION)) {
            controller.showMessage("Internal error");
            return;
        }

        // Transform string to int
        String[] chunks = msg.split(",");
        int[] ids = new int[chunks.length];
        for(int i = 0; i < chunks.length; i++){
            try{ ids[i] = Integer.parseInt(chunks[i]); } catch (Exception ignored){}
        }

        // get DB ids as Clothing objects
        ClothingDao dao = new ClothingDao();
        recomendations = dao.getFromIds(ids);

        controller.showRecomendations();
        controller.showMessage("I' ve found " + recomendations.size() + " cloths for you, I hope you like them.");
    }
}
