package logic.service.itemSelectorService;

import control.GuiLauncher;
import control.ItemController;
import control.ItemSelectorController;
import dataAccess.ClothingDao;
import dataAccess.CustomerDao;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import logic.transfer.CustomerResponse;
import logic.agents.chatbotAgent.ChatbotAgent;
import logic.agents.chatbotAgent.Intents;
import logic.agents.guiAgent.GuiAgent;
import logic.agents.recommenderAgent.RecommenderAgent;
import logic.agents.recommenderAgent.RecommenderMsg;
import logic.agents.recommenderAgent.pythonArgsAdapter.ColaborativeFilterAdapter;
import logic.agents.recommenderAgent.pythonArgsAdapter.ContentBasedAdapter;
import logic.service.IService;
import logic.transfer.Clothing;
import logic.transfer.Customer;

import java.io.IOException;
import java.util.ArrayList;

public class ItemSelectorService implements IService {

    private static final String ERROR_RECOMMENDATION = "error";
    private Customer customer;
    private ArrayList<Clothing> clothings;
    private ArrayList<Clothing> recomendations;
    private boolean showRecomendations;
    private ItemSelectorController controller;
    private GuiAgent guiAgent;
    private DfaItemSelectorService dfa;

    public ItemSelectorService(Customer customer, ItemSelectorController controller, GuiAgent guiAgent) {
        this.customer = customer;
        this.controller = controller;
        this.guiAgent = guiAgent;
        this.showRecomendations = false;

        ClothingDao dao = new ClothingDao();
        this.clothings = new ArrayList<>(dao.getAll());

        this.dfa = new DfaItemSelectorService(this);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setup(){
        dfa.runDFA("", true);
    }

    public int getNumberItems(){
        ArrayList<Clothing> cs = (recomendations == null || !showRecomendations) ? clothings : recomendations;
        return cs.size();
    }

    public Clothing getItem(int i){
        ArrayList<Clothing> cs = (recomendations == null || !showRecomendations) ? clothings : recomendations;

        if(i < 0 || i >= cs.size())
            return new Clothing();

        return cs.get(i);
    }

    public void switchCloths(){
        if(recomendations == null)
            return;

        showRecomendations = !showRecomendations;
        controller.refreshItems();
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

    public void showMessage(String msg){
        if(msg != null && !msg.equals(""))
            controller.showMessage(msg);
    }

    public void notifyChatbotAgent(String str){
        GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_CHATBOT);
        ge.addParameter(str);
        guiAgent.postGuiEvent(ge);
    }

    public void notifyCBRecommender(boolean addGender){
        ContentBasedAdapter adapter = new ContentBasedAdapter(customer, addGender);
        RecommenderMsg rMsg = new RecommenderMsg(RecommenderMsg.CONTENT_BASED_TYPE, adapter.getPythonArgs());
        GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_RECOMMENDER);
        ge.addParameter(rMsg.getRawMsg());
        guiAgent.postGuiEvent(ge);
    }

    public void notifyCFRecommender(){
        ColaborativeFilterAdapter adapter = new ColaborativeFilterAdapter(customer);
        RecommenderMsg rMsg = new RecommenderMsg(RecommenderMsg.COLABORATIVE_FILTER_TYPE, adapter.getPythonArgs());
        GuiEvent ge = new GuiEvent(this, GuiAgent.CMD_SEND_RECOMMENDER);
        ge.addParameter(rMsg.getRawMsg());
        guiAgent.postGuiEvent(ge);
    }

    public void onClickSend(String msg){
        dfa.runDFA(msg, false);
    }

    @Override
    public void handleACLMsg(ACLMessage msg) {
        if(msg.getSender().getName().contains(ChatbotAgent.NAME))
            handleChatbotMsg(msg.getContent());

        else if(msg.getSender().getName().contains(RecommenderAgent.NAME))
            handleRecommenderMsg(msg.getContent());
    }

    private void handleChatbotMsg(String msg) {
        dfa.runDFA(msg, true);
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

        controller.selectRecommendationToggle();
        controller.showMessage("I' ve found " + recomendations.size() + " clothe(s) for you, I hope you like them.");
        controller.showMessage("Talk to me for more advises.");
    }
}
